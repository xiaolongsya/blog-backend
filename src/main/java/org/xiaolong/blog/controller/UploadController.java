package org.xiaolong.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xiaolong.blog.common.Result;
import org.xiaolong.blog.utils.UploadUtils;

import java.io.IOException;

@RestController
@RequestMapping("/upload")
public class UploadController
{
    @Autowired
    private UploadUtils uploadUtils;
    @Value("${upload.max-size}")
    private Integer maxSize;

    //上传图片接口,返回url
    @RequestMapping("/image")
    @Operation(summary = "上传图片接口", description = "上传图片接口，返回url")
    public Result<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            // 校验文件大小
            if (file.getSize() > maxSize)
            {
                return Result.error(400, "文件过大，最大支持5MB");
            }

            // 调用工具类上传
            String imageUrl = uploadUtils.uploadImage(file);
            return Result.success(imageUrl);
        } catch (IllegalArgumentException e)
        {
            return Result.error(400, e.getMessage());
        } catch (IOException e) {
            return Result.error(500, "图片上传失败：" + e.getMessage());
        }
    }
}
