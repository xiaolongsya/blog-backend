package org.xiaolong.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    @RequestMapping("/list")
    @Operation(summary = "获取所有分类")
    public List<Growth> listGrowth()
    {
        return growthService.listGrowth();
    }
}
