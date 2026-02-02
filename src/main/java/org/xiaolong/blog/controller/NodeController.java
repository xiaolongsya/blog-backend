package org.xiaolong.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    @RequestMapping("/list")
    @Operation(summary = "根据分类id查询节点")
    public List<Node> listNodeByGrowthId(@Parameter(description = "分类id") Long growthId)
    {
        return nodeService.listNodesByGrowthId(growthId);
    }
}
