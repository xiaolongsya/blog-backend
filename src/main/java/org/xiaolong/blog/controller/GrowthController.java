package org.xiaolong.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xiaolong.blog.common.Result;
import org.xiaolong.blog.entity.Growth;
import org.xiaolong.blog.service.GrowthService;

import java.util.List;

@RestController
@RequestMapping("/growth")
public class GrowthController
{
    @Autowired
    private GrowthService growthService;

    // 获取所有分类
    @GetMapping("/list")
    @Operation(summary = "获取所有分类")
    public Result<List<Growth>> listGrowth()
    {
        return Result.success(growthService.listGrowth());
    }

    //上传分类
    @PostMapping("/upload")
    @Operation(summary = "上传分类")
    public Result<String> uploadGrowth(@RequestBody Growth growth)
    {
        return Result.success(growthService.uploadGrowth(growth));
    }

    //删除分类
    @PostMapping("/delete")
    @Operation(summary = "删除分类")
    public Result<String> deleteGrowth(@RequestParam Long id)
    {
        return Result.success(growthService.deleteGrowth(id));
    }
}
