package org.xiaolong.blog.service.iml;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xiaolong.blog.common.BusinessException;
import org.xiaolong.blog.entity.Growth;
import org.xiaolong.blog.entity.Node;
import org.xiaolong.blog.mapper.NodeMapper;
import org.xiaolong.blog.service.GrowthService;
import org.xiaolong.blog.service.NodeService;

import java.util.List;

@Service
public class NodeServiceIml implements NodeService
{
    @Autowired
    private NodeMapper nodeMapper;
    @Autowired
    private GrowthService growthService;

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

    //上传节点
    @Override
    public String uploadNode(Node node) throws BusinessException
    {
        try
        {
            nodeMapper.insert(node);
            Growth growth = growthService.getGrowthById(node.getGrowthId());
            growth.setNodeCount(growth.getNodeCount() + 1);
            growth.setLastNode(node.getId());
            Integer result = growthService.updateGrowth(growth);
            if(result <= 0)
            {
                throw new BusinessException(500, "更新分类失败");
            }
            return "上传成功";
        } catch (Exception e)
        {
            throw new BusinessException(500, "上传节点失败" + e.getMessage());
        }
    }

    //删除节点
    @Override
    public String deleteNode(Long id) throws BusinessException
    {
        try
        {
            Node node = nodeMapper.selectById(id);
            Growth growth = growthService.getGrowthById(node.getGrowthId());
            growth.setNodeCount(growth.getNodeCount() - 1);
            Integer result = growthService.updateGrowth(growth);
            if(result <= 0)
            {
                throw new BusinessException(500, "更新分类失败");
            }
            nodeMapper.deleteById(id);
            return "删除成功";
        } catch (Exception e)
        {
            throw new BusinessException(500, "删除节点失败" + e.getMessage());
        }
    }

    //分页查询节点
    @Override
    public List<Node> listNodePage(int pageNum, int pageSize, Long growth_id) throws BusinessException
    {
        try
        {
            Page<Node> pageParam = new Page<>(pageNum, pageSize);
            IPage<Node> result = nodeMapper.listNodesByPage(pageParam, growth_id);
            return result.getRecords();
        } catch (Exception e)
        {
            throw new BusinessException(500, "分页查询节点列表失败" + e.getMessage());
        }
    }
}
