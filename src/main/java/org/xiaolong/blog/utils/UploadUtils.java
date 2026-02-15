package org.xiaolong.blog.utils;

import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Component
public class UploadUtils {
    private static final Logger log = LoggerFactory.getLogger(UploadUtils.class);

    @Value("${upload.base-path}")
    private String basePath;

    @Value("${upload.access-prefix}")
    private String accessPrefix;

    // 图片压缩配置（JPG格式，兼容所有环境，无任何兼容问题）
    private static final int MAX_WIDTH = 1920; // 最大宽度（保证文字清晰）
    private static final float QUALITY = 0.95f; // 压缩质量（90%，肉眼无差别）
    private static final String TARGET_FORMAT = "jpg"; // 彻底改为JPG格式

    public String uploadImage(MultipartFile file) throws IOException {
        log.info("配置的文件存储根路径：{}", basePath);
        log.info("配置的访问前缀：{}", accessPrefix);

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new IOException("文件名称为空");
        }
        log.info("上传文件原名称：{}", originalFilename);

        // 生成JPG格式新文件名
        String newFileName = UUID.randomUUID().toString() + "." + TARGET_FORMAT;
        log.info("生成的新文件名（{}格式）：{}", TARGET_FORMAT, newFileName);

        File saveDir = new File(basePath);
        File saveFile = new File(basePath, newFileName);
        log.info("目标存储目录：{}", saveDir.getAbsolutePath());
        log.info("目标文件完整路径：{}", saveFile.getAbsolutePath());

        if (!saveDir.exists()) {
            boolean mkdirsResult = saveDir.mkdirs();
            log.info("目录不存在，尝试创建目录，创建结果：{}", mkdirsResult);
        } else {
            log.info("目录已存在，无需创建");
        }

        boolean dirWritable = saveDir.canWrite();
        log.info("目标目录是否可写：{}", dirWritable);
        if (!dirWritable) {
            log.error("目标目录不可写，无法保存文件！");
            throw new IOException("目标目录不可写，权限不足");
        }

        log.info("开始处理图片压缩，原文件大小：{} 字节", file.getSize());
        try (InputStream in = file.getInputStream()) {
            BufferedImage originalImage = ImageIO.read(in);
            if (originalImage == null) {
                throw new IOException("文件不是有效图片格式（仅支持JPG/PNG/GIF等）");
            }
            log.info("原图尺寸：宽{}px × 高{}px", originalImage.getWidth(), originalImage.getHeight());

            // 压缩并转换为JPG格式（原生支持，无任何兼容问题）
            Thumbnails.of(originalImage)
                    .size(MAX_WIDTH, Integer.MAX_VALUE) // 等比缩放，只限制宽度
                    .outputQuality(QUALITY) // 80%质量，保证文字清晰
                    .outputFormat(TARGET_FORMAT) // JPG格式
                    .toFile(saveFile);

            // 校验压缩后文件
            if (!saveFile.exists() || saveFile.length() == 0) {
                throw new IOException("图片压缩后文件为空，请检查图片格式");
            }
            log.info("图片压缩完成，压缩后文件大小：{} 字节", saveFile.length());
            log.info("压缩后文件是否存在：{}", saveFile.exists());
        } catch (Exception e) {
            log.error("图片压缩失败", e);
            // 移除WebP相关提示，只保留通用报错
            throw new IOException("图片压缩失败：" + e.getMessage());
        }

        // 构建访问URL
        String accessUrl = accessPrefix + newFileName;
        log.info("构建的访问 URL：{}", accessUrl);
        return accessUrl;
    }
}