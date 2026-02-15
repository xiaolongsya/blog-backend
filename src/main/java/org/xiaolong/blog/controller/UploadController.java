package org.xiaolong.blog.controller;


import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xiaolong.blog.common.Result;
import org.xiaolong.blog.entity.Img; // 导入 Img 实体
import org.xiaolong.blog.interceptor.RateLimit;
import org.xiaolong.blog.mapper.ImgMapper;
import org.xiaolong.blog.utils.UploadUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

@RestController
@RequestMapping("/upload")
public class UploadController
{
    // 日志组件（替代 System.err，便于线上排查）
    private static final Logger log = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    private UploadUtils uploadUtils;

    @Autowired
    private ImgMapper imgMapper; // 注入 ImgMapper

    @Value("${upload.max-size}")
    private Integer maxSize;

    // 上传图片接口,返回url
    @RateLimit(count = 30, time = 60000)
    @PostMapping("/image")
    @Operation(summary = "上传图片接口", description = "上传图片接口，返回url")
    public Result<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            // 1. 校验文件大小（单位：字节，和配置文件一致）
            if (file.getSize() > maxSize)
            {
                return Result.error(400, "文件过大，最大支持10MB");
            }

            // 2. 调用工具类完成图片上传，获取访问URL
            String imageUrl = uploadUtils.uploadImage(file);

            // 3. 组装 Img 实体，写入数据库
            // 3.1 字节转KB（保留2位小数，避免精度丢失）
            long fileSizeBytes = file.getSize();
            BigDecimal sizeKB = new BigDecimal(fileSizeBytes)
                    .divide(new BigDecimal(1024), 2, RoundingMode.HALF_UP);

            // 3.2 构建实体对象（字段完全对应）
            Img img = new Img();
            img.setUrl(imageUrl); // 对应数据库 url 字段
            img.setSize(sizeKB.intValue()); // 转int存入（如需小数可改表字段为 decimal）

            // 3.3 插入数据库（BaseMapper.insert 返回受影响行数）
            int affectedRows = imgMapper.insert(img);
            if (affectedRows != 1) {
                log.warn("图片上传成功，但入库失败，URL：{}", imageUrl);
            }

            // 4. 返回上传成功的URL
            return Result.success(imageUrl);
        } catch (IllegalArgumentException e)
        {
            return Result.error(400, e.getMessage());
        } catch (IOException e) {
            log.error("图片上传失败", e); // 记录异常栈，便于排查
            return Result.error(500, "图片上传失败：" + e.getMessage());
        } catch (Exception e) {
            log.error("图片入库异常", e);
            // 入库失败不影响图片上传主流程，尝试返回URL
            try {
                String imageUrl = uploadUtils.uploadImage(file);
                return Result.success(imageUrl);
            } catch (Exception ex) {
                return Result.error(500, "图片上传及入库均失败：" + ex.getMessage());
            }
        }
    }

}