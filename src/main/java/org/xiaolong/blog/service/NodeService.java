package org.xiaolong.blog.service;

import org.xiaolong.blog.common.BusinessException;
import org.xiaolong.blog.dto.GrowthSearchDTO;
import org.xiaolong.blog.entity.Node;

import java.util.List;

public interface NodeService
{

    //获取分类的节点列表
    List<Node> listNodesByGrowthId(Long growthId) throws BusinessException;
    //上传节点
    String uploadNode(Node node) throws BusinessException;

    String deleteNode(Long id) throws BusinessException;

    List<Node> listNodePage(int pageNum, int pageSize, Long growthId) throws BusinessException;

    List<GrowthSearchDTO> listNodeByKeyword(String keyword) throws BusinessException;
}
