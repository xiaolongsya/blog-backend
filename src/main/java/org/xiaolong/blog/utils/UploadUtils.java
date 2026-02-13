package org.xiaolong.blog.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Component
public class UploadUtils {
    // 1. 添加日志对象
    private static final Logger log = LoggerFactory.getLogger(UploadUtils.class);

    @Value("${upload.base-path}")
    private String basePath;

    @Value("${upload.access-prefix}")
    private String accessPrefix;

    //上传图片
    public String uploadImage(MultipartFile file) throws IOException {
        // 2. 打印配置的 basePath（确认是否读取到正确的服务器路径）
        log.info("配置的文件存储根路径：{}", basePath);
        log.info("配置的访问前缀：{}", accessPrefix);

        // 3. 获取原文件名和后缀
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.lastIndexOf(".") == -1) {
            throw new IOException("文件格式错误，无后缀名");
        }
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        log.info("上传文件原名称：{}，后缀：{}", originalFilename, suffix);

        // 4. 生成新文件名
        String newFileName = UUID.randomUUID().toString() + suffix;
        log.info("生成的新文件名：{}", newFileName);

        // 5. 构建目标文件对象和目录对象
        File saveDir = new File(basePath);
        File saveFile = new File(basePath, newFileName); // 更安全的路径拼接方式
        log.info("目标存储目录：{}", saveDir.getAbsolutePath());
        log.info("目标文件完整路径：{}", saveFile.getAbsolutePath());

        // 6. 检查并创建目录（打印创建结果）
        if (!saveDir.exists()) {
            boolean mkdirsResult = saveDir.mkdirs();
            log.info("目录不存在，尝试创建目录，创建结果：{}", mkdirsResult);
        } else {
            log.info("目录已存在，无需创建");
        }

        // 7. 检查目录是否可写（关键：判断目录权限）
        boolean dirWritable = saveDir.canWrite();
        log.info("目标目录是否可写：{}", dirWritable);
        if (!dirWritable) {
            log.error("目标目录不可写，无法保存文件！");
            throw new IOException("目标目录不可写，权限不足");
        }

        // 8. 写入文件（用 try-with-resources 确保流关闭，打印写入前后的文件状态）
        log.info("开始写入文件，文件大小：{} 字节", file.getSize());
        try (InputStream in = file.getInputStream()) {
            org.apache.commons.io.FileUtils.copyInputStreamToFile(in, saveFile);
        }
        log.info("文件写入完成，检查文件是否存在：{}", saveFile.exists());
        log.info("文件写入后大小：{} 字节", saveFile.length());

        // 9. 构建并返回访问 URL
        String accessUrl = accessPrefix + newFileName;
        log.info("构建的访问 URL：{}", accessUrl);
        return accessUrl;
    }

}