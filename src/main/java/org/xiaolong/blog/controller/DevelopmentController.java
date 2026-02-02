package org.xiaolong.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaolong.blog.common.Result;
import org.xiaolong.blog.entity.Development;
import org.xiaolong.blog.service.DevelopmentService;

import java.util.List;

@RestController
@RequestMapping("/development")
public class DevelopmentController
{
    @Autowired
    private DevelopmentService developmentService;

    //上传更新记录
    @RequestMapping("/upload")
    @Operation(summary = "上传更新记录")
    public Result<Long> upload(@RequestBody Development development)
    {
        return Result.success(developmentService.upload(development), "上传成功");
    }

    //查询更新记录
    @RequestMapping("/list")
    @Operation(summary = "查询更新记录")
    public Result<List<Development>> list()
    {
        return Result.success(developmentService.list(), "查询成功");
    }
}
