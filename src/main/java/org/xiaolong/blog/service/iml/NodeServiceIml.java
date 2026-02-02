package org.xiaolong.blog.service.iml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xiaolong.blog.common.BusinessException;
import org.xiaolong.blog.entity.Node;
import org.xiaolong.blog.mapper.NodeMapper;
import org.xiaolong.blog.service.NodeService;

import java.util.List;

@Service
public class NodeServiceIml implements NodeService
{
    @Autowired
    private NodeMapper nodeMapper;

    //根据分类id查询节点
    @Override
    public List<Node> listNodesByGrowthId(Long growth_id) throws BusinessException
    {
        try
        {
            return nodeMapper.listNodesByGrowthId(growth_id);
        } catch (Exception e)
        {
            throw new BusinessException(500, "查询节点列表失败" + e.getMessage());
        }
    }
}
