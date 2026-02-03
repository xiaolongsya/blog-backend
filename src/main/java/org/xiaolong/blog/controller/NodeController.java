package org.xiaolong.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xiaolong.blog.common.Result;
import org.xiaolong.blog.entity.Node;
import org.xiaolong.blog.service.NodeService;

import java.util.List;

@RestController
@RequestMapping("/node")
public class NodeController
{
    @Autowired
    private NodeService nodeService;

    //根据分类id查询节点
    @GetMapping("/list")
    @Operation(summary = "根据分类id查询节点")
    public Result<List<Node>> listNodeByGrowthId(@Parameter(description = "分类id") Long growthId)
    {
        return Result.success(nodeService.listNodesByGrowthId(growthId));
    }

    //上传节点
    @PostMapping("/upload")
    @Operation(summary = "上传节点")
    public Result<String> uploadNode(@RequestBody Node node)
    {
        return Result.success(nodeService.uploadNode(node));
    }
}
