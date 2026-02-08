package org.xiaolong.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xiaolong.blog.common.Result;
import org.xiaolong.blog.entity.Recent;
import org.xiaolong.blog.service.RecentService;

import java.util.List;

@RestController
@RequestMapping("/recent")
public class RecentController
{
    @Autowired
    private RecentService recentService;

    //获取最近列表
    @GetMapping("/list")
    @Operation(summary = "获取最近列表")
    public Result<List<Recent>> listRecent()
    {
        return Result.success(recentService.listRecent(), "获取最近列表成功");
    }

    //上传最近事项
    @PostMapping("/upload")
    @Operation(summary = "上传最近列表")
    public Result<String> uploadRecent(@RequestBody Recent recent)
    {
        return Result.success(recentService.uploadRecent(recent));
    }

    //删除最近事项（传入id）
    @PostMapping("/delete")
    @Operation(summary = "删除最近事项", description = "传入id")
    public Result<String> deleteRecent(@RequestParam Long id)
    {
        return Result.success(recentService.deleteRecent(id));
    }
}
