package org.xiaolong.blog.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UnusedImageCleaner {
    private static final Logger log = LoggerFactory.getLogger(UnusedImageCleaner.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // ========== 精准适配你的配置 ==========
    // 服务器图片存储绝对路径
    private static final String IMAGE_FOLDER_PATH = "/opt/1panel/www/sites/dragon-blog-static/index/uploads";
    // 废弃图片归档目录
    private static final String ARCHIVE_FOLDER_PATH = "/opt/1panel/www/sites/dragon-blog-static/index/uploads_unused";
    // 检测报告生成路径
    private static final String REPORT_FILE_PATH = "/opt/1panel/www/sites/dragon-blog-static/index/unused_images_report.txt";
    // 图片URL访问前缀（必须和content里的一致）
    private static final String ACCESS_PREFIX = "https://xiaolongya.cn/uploads/";
    // 支持的图片格式
    private static final List<String> IMAGE_FORMATS = Arrays.asList("jpg", "jpeg", "png", "webp", "gif", "bmp");

    // ========== 安全防护配置（核心！防止误删） ==========
    // 安全模式：true=仅生成报告，禁止自动归档；false=允许归档（需手动开启）
    private static final boolean SAFE_MODE = false;
    // 最小文件大小：小于10KB的图片不处理（避免误删小图标/静态资源）
    private static final long MIN_FILE_SIZE = 10 * 1024;
    // 匹配 [IMAGE:URL] 格式的正则（适配dragon_node的content字段）
    private static final Pattern IMAGE_TAG_PATTERN = Pattern.compile("\\[IMAGE:(https?://xiaolongya.cn/uploads/[^\\]]+)\\]");
    // 匹配纯文本完整URL的正则（https://xxx/uploads/xxx.png）
    private static final Pattern FULL_URL_PATTERN = Pattern.compile("https?://xiaolongya.cn/uploads/[^\\s,\"'\\]<>]+\\.(jpg|jpeg|png|webp|gif|bmp)");
    // 匹配相对路径URL的正则（/uploads/xxx.png）
    private static final Pattern RELATIVE_URL_PATTERN = Pattern.compile("/uploads/[^\\s,\"'\\]<>]+\\.(jpg|jpeg|png|webp|gif|bmp)");

    /**
     * 核心方法：检测并归档未使用的图片
     * @param archive 是否归档（true=移动到归档目录，false=仅生成报告）
     */
    public void detectAndArchiveUnusedImages(boolean archive) {
        try {
            log.info("========== 开始检测废弃图片（安全模式开启） ==========");

            // 安全防护1：SAFE_MODE=true时，禁止任何归档操作
            if (SAFE_MODE && archive) {
                log.error("⚠️ 安全模式已开启，禁止自动归档！如需归档，请先将SAFE_MODE改为false并人工核对报告");
                return;
            }

            // 1. 从所有表中提取【所有被引用】的图片文件名（兼容所有URL格式）
            Set<String> usedImageNames = getAllUsedImageNamesFromDB();
            log.info("✅ 从数据库中提取到 {} 个被引用的图片文件名", usedImageNames.size());

            // 2. 扫描服务器图片目录（过滤小文件，避免误删静态资源）
            Set<String> allImageNames = scanAllImageNamesInFolder();
            log.info("✅ 服务器图片目录下共有 {} 个有效图片文件（过滤小文件后）", allImageNames.size());

            // 3. 对比找出真正未使用的图片
            Set<String> unusedImageNames = new HashSet<>(allImageNames);
            unusedImageNames.removeAll(usedImageNames);
            log.info("⚠️ 检测到 {} 个未被引用的废弃图片（请人工核对）", unusedImageNames.size());

            // 4. 生成详细检测报告（包含文件大小、引用位置）
            generateReport(usedImageNames, allImageNames, unusedImageNames);
            log.info("✅ 检测报告已生成：{}", REPORT_FILE_PATH);

            // 5. 归档废弃图片（双重确认）
            if (archive && !unusedImageNames.isEmpty()) {
                log.warn("⚠️ 即将归档 {} 个废弃图片，5秒后开始（按Ctrl+C可中断）...", unusedImageNames.size());
                Thread.sleep(5000); // 人工中断窗口期
                archiveUnusedImages(unusedImageNames);
                log.info("✅ 已将 {} 个废弃图片归档到：{}", unusedImageNames.size(), ARCHIVE_FOLDER_PATH);
            }

            log.info("========== 废弃图片检测完成 ==========");
        } catch (InterruptedException e) {
            log.info("⚠️ 用户中断了归档操作，已取消");
        } catch (Exception e) {
            log.error("检测/归档废弃图片失败", e);
        }
    }

    /**
     * 从所有相关表提取【所有被引用】的图片文件名
     * 覆盖：dragon_comment、dragon_comment_reply、dragon_development、dragon_node、dragon_stack、dragon_img
     */
    private Set<String> getAllUsedImageNamesFromDB() {
        Set<String> usedImageNames = new HashSet<>();

        // 1. dragon_comment（img_urls字段：逗号分隔的URL）
        addImageNamesFromTable(usedImageNames, "dragon_comment", "img_urls", true, false);

        // 2. dragon_comment_reply（content字段：可能包含[IMAGE:URL]/纯文本URL）
        addImageNamesFromTable(usedImageNames, "dragon_comment_reply", "content", false, true);

        // 3. dragon_development（img_urls字段：逗号分隔的URL）
        addImageNamesFromTable(usedImageNames, "dragon_development", "img_urls", true, false);

        // 4. dragon_node（核心！content含[IMAGE:URL]/纯文本URL + img_urls含逗号分隔URL）
        addImageNamesFromTable(usedImageNames, "dragon_node", "content", false, true); // 解析所有URL格式
        addImageNamesFromTable(usedImageNames, "dragon_node", "img_urls", true, false); // 解析逗号分隔URL

        // 5. dragon_stack（img_url字段：单个URL）
        addImageNamesFromTable(usedImageNames, "dragon_stack", "img_url", false, false);

        // 6. dragon_img（url字段：图片统一管理，避免误删）
        addImageNamesFromTable(usedImageNames, "dragon_img", "url", false, false);

        // 7. dragon_recent（content字段：可能包含[IMAGE:URL]/纯文本URL）
        addImageNamesFromTable(usedImageNames, "dragon_recent", "content", false, true);

        return usedImageNames;
    }

    /**
     * 从指定表的指定字段提取图片文件名（兼容所有URL格式）
     * @param usedImageNames 存储结果的集合
     * @param tableName 表名
     * @param columnName 字段名
     * @param isMultiUrl 是否多URL（逗号分隔）
     * @param hasImageTag 是否包含[IMAGE:URL]格式
     */
    private void addImageNamesFromTable(Set<String> usedImageNames, String tableName, String columnName,
                                        boolean isMultiUrl, boolean hasImageTag) {
        try {
            // 查询非空字段值
            String sql = String.format("SELECT %s FROM %s WHERE %s IS NOT NULL AND %s != ''",
                    columnName, tableName, columnName, columnName);
            List<String> columnValues = jdbcTemplate.queryForList(sql, String.class);
            int extractedCount = 0;

            for (String value : columnValues) {
                if (value == null || value.isEmpty()) continue;

                // 预处理：去除所有空格/换行/制表符，避免解析失败
                String cleanValue = value.replaceAll("\\s+", "").trim();
                if (cleanValue.isEmpty()) continue;

                // 1. 解析[IMAGE:URL]格式（优先）
                if (hasImageTag) {
                    Matcher matcher = IMAGE_TAG_PATTERN.matcher(cleanValue);
                    while (matcher.find()) {
                        String url = matcher.group(1).trim();
                        String imageName = extractImageNameFromUrl(url);
                        if (imageName != null) {
                            usedImageNames.add(imageName);
                            extractedCount++;
                        }
                    }
                }

                // 2. 解析纯文本完整URL（https://xxx/uploads/xxx.png）
                Matcher fullUrlMatcher = FULL_URL_PATTERN.matcher(cleanValue);
                while (fullUrlMatcher.find()) {
                    String url = fullUrlMatcher.group(0).trim();
                    String imageName = extractImageNameFromUrl(url);
                    if (imageName != null) {
                        usedImageNames.add(imageName);
                        extractedCount++;
                    }
                }

                // 3. 解析相对路径URL（/uploads/xxx.png）
                Matcher relativeUrlMatcher = RELATIVE_URL_PATTERN.matcher(cleanValue);
                while (relativeUrlMatcher.find()) {
                    String relativeUrl = relativeUrlMatcher.group(0).trim();
                    // 拼接成完整URL
                    String fullUrl = ACCESS_PREFIX + relativeUrl.replace("/uploads/", "");
                    String imageName = extractImageNameFromUrl(fullUrl);
                    if (imageName != null) {
                        usedImageNames.add(imageName);
                        extractedCount++;
                    }
                }

                // 4. 解析逗号分隔的URL（img_urls字段专用）
                if (isMultiUrl) {
                    List<String> urlList = Arrays.asList(cleanValue.split(","));
                    for (String url : urlList) {
                        url = url.trim();
                        if (url.isEmpty()) continue;
                        String imageName = extractImageNameFromUrl(url);
                        if (imageName != null) {
                            usedImageNames.add(imageName);
                            extractedCount++;
                        }
                    }
                }
            }

            log.info("✅ 从 {} 表的 {} 字段提取到 {} 个图片名",
                    tableName, columnName, extractedCount);
        } catch (Exception e) {
            log.error("❌ 处理 {} 表的 {} 字段失败", tableName, columnName, e);
        }
    }

    /**
     * 从URL中提取图片文件名（兼容完整URL/相对路径）
     * @param url 图片URL（完整/相对路径）
     * @return 图片文件名（如 123.png），无效则返回null
     */
    private String extractImageNameFromUrl(String url) {
        if (url == null || url.isEmpty()) return null;

        // 兼容相对路径（/uploads/xxx.png → 拼接完整URL）
        String fullUrl = url.startsWith("http") ? url : ACCESS_PREFIX + url.replace("/uploads/", "");

        // 提取文件名（最后一个/后的部分）
        if (!fullUrl.contains(ACCESS_PREFIX)) return null;
        String imageName = fullUrl.substring(fullUrl.lastIndexOf("/") + 1);

        // 过滤非法字符（引号、括号、逗号等）
        imageName = imageName.replaceAll("[\"'\\]<>(),;]", "");

        // 验证文件后缀是否合法
        if (imageName.contains(".") && imageName.lastIndexOf(".") != imageName.length() - 1) {
            String suffix = imageName.substring(imageName.lastIndexOf(".") + 1).toLowerCase();
            if (IMAGE_FORMATS.contains(suffix)) {
                return imageName;
            }
        }
        return null;
    }

    /**
     * 扫描服务器图片目录，过滤小文件和非图片
     */
    private Set<String> scanAllImageNamesInFolder() {
        Set<String> allImageNames = new HashSet<>();
        File folder = new File(IMAGE_FOLDER_PATH);

        if (!folder.exists() || !folder.isDirectory()) {
            log.error("❌ 图片目录不存在或不是目录：{}", IMAGE_FOLDER_PATH);
            return allImageNames;
        }

        File[] files = folder.listFiles();
        if (files == null) return allImageNames;

        for (File file : files) {
            // 过滤：非文件、隐藏文件、小文件（小于10KB）
            if (!file.isFile() || file.isHidden() || file.length() < MIN_FILE_SIZE) {
                continue;
            }

            String fileName = file.getName();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            if (IMAGE_FORMATS.contains(suffix)) {
                allImageNames.add(fileName);
            }
        }

        return allImageNames;
    }

    /**
     * 生成详细检测报告（含文件大小、安全提示）
     */
    private void generateReport(Set<String> used, Set<String> all, Set<String> unused) throws IOException {
        File reportFile = new File(REPORT_FILE_PATH);
        File parentDir = reportFile.getParentFile();
        if (!parentDir.exists()) parentDir.mkdirs();

        try (FileWriter writer = new FileWriter(reportFile)) {
            writer.write("===== 小龙博客废弃图片检测报告（精准版） =====\n");
            writer.write("检测时间：" + new Date() + "\n");
            writer.write("安全模式：" + (SAFE_MODE ? "开启（禁止自动归档）" : "关闭") + "\n");
            writer.write("最小文件大小阈值：" + MIN_FILE_SIZE/1024 + "KB\n");
            writer.write("URL兼容：[IMAGE:URL]、完整URL、相对路径均支持\n");
            writer.write("============================================\n");
            writer.write("服务器图片存储目录：" + IMAGE_FOLDER_PATH + "\n");
            writer.write("废弃图片归档目录：" + ARCHIVE_FOLDER_PATH + "\n");
            writer.write("============================================\n");
            writer.write("统计信息：\n");
            writer.write("- 服务器有效图片总数：" + all.size() + "\n");
            writer.write("- 已引用图片数：" + used.size() + "\n");
            writer.write("- 疑似废弃图片数：" + unused.size() + "\n");
            writer.write("============================================\n");

            if (!unused.isEmpty()) {
                writer.write("疑似废弃图片列表（共" + unused.size() + "个）：\n");
                int index = 1;
                for (String name : unused) {
                    File file = new File(IMAGE_FOLDER_PATH, name);
                    String size = file.exists() ? (file.length()/1024 + "KB") : "文件不存在";
                    writer.write(index + ". " + name + "（大小：" + size + "）\n");
                    index++;
                }
                writer.write("============================================\n");
                writer.write("⚠️ 重要提示：\n");
                writer.write("1. 请人工核对以上列表，确认无业务引用后再手动归档；\n");
                writer.write("2. 所有格式的图片URL（[IMAGE:URL]/完整/相对路径）已被识别；\n");
                writer.write("3. 如需自动归档，请将SAFE_MODE改为false后重试。\n");
            } else {
                writer.write("✅ 未检测到废弃图片，所有图片均被业务引用！\n");
            }
        }
    }

    /**
     * 归档废弃图片（移动到归档目录，不直接删除）
     */
    private void archiveUnusedImages(Set<String> unusedNames) throws IOException {
        File archiveFolder = new File(ARCHIVE_FOLDER_PATH);
        if (!archiveFolder.exists()) {
            archiveFolder.mkdirs();
            log.info("✅ 创建归档目录：{}", ARCHIVE_FOLDER_PATH);
        }

        for (String fileName : unusedNames) {
            File sourceFile = new File(IMAGE_FOLDER_PATH, fileName);
            File targetFile = new File(ARCHIVE_FOLDER_PATH, fileName);
            if (sourceFile.exists()) {
                Files.move(sourceFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                log.info("✅ 归档图片：{} -> {}", fileName, ARCHIVE_FOLDER_PATH);
            } else {
                log.warn("⚠️ 图片文件不存在，跳过归档：{}", fileName);
            }
        }
    }

    // ========== 测试入口（本地/服务器均可） ==========
    public static void main(String[] args) {
        // 本地测试需初始化Spring上下文，服务器无需此代码
        // ApplicationContext context = new AnnotationConfigApplicationContext(你的Spring配置类.class);
        // UnusedImageCleaner cleaner = context.getBean(UnusedImageCleaner.class);

        // 第一步：仅生成报告（优先执行）
        // cleaner.detectAndArchiveUnusedImages(false);

        // 第二步：确认报告无误后，改为SAFE_MODE=false，执行归档
        // cleaner.detectAndArchiveUnusedImages(true);
    }
}