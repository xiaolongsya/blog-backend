package org.xiaolong.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xiaolong.blog.common.Result;
import org.xiaolong.blog.dto.GrowthSearchDTO;
import org.xiaolong.blog.entity.Node;
import org.xiaolong.blog.interceptor.RateLimit;
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

    //删除节点
    @PostMapping("/delete")
    @Operation(summary = "删除节点")
    public Result<String> deleteNode(@RequestParam Long id)
    {
        return Result.success(nodeService.deleteNode(id));
    }


    //分页查询节点
    @RateLimit(count = 30, time = 60000)
    @GetMapping("/listPage")
    @Operation(summary = "分页查询节点", description = "传入页码和大小")
    public Result<List<Node>> listNodePage(@RequestParam int pageNum, @RequestParam int pageSize, @RequestParam Long growthId)
    {
        return Result.success(nodeService.listNodePage(pageNum, pageSize, growthId));
    }

    //传入关键字查询节点
    @GetMapping("/listByKeyword")
    @Operation(summary = "传入关键字查询节点")
    public Result<List<GrowthSearchDTO>> listNodeByKeyword(@RequestParam String keyword)
    {
        return Result.success(nodeService.listNodeByKeyword(keyword));
    }
}
