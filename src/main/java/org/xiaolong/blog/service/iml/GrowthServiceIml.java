package org.xiaolong.blog.service.iml;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xiaolong.blog.common.BusinessException;
import org.xiaolong.blog.entity.Growth;
import org.xiaolong.blog.entity.Node;
import org.xiaolong.blog.mapper.GrowthMapper;
import org.xiaolong.blog.mapper.NodeMapper;
import org.xiaolong.blog.service.GrowthService;
import org.xiaolong.blog.service.NodeService;

import java.util.List;

@Service
public class GrowthServiceIml implements GrowthService
{
    @Autowired
    private GrowthMapper growthMapper;
    @Autowired
    private NodeMapper nodeMapper;
    //获取分类列表
    @Override
    public List<Growth> listGrowth() throws BusinessException
    {
        try
        {
            return growthMapper.selectList(null);
        } catch (Exception e)
        {
            throw new BusinessException(500, "查询分类列表失败" + e.getMessage());
        }
    }

    //上传分类
    @Override
    public String uploadGrowth(Growth growth) throws BusinessException
    {
        try
        {
            growthMapper.insert(growth);
            return "上传成功";
        } catch (Exception e)
        {
            throw new BusinessException(500, "上传分类失败" + e.getMessage());
        }
    }

    //根据id查询分类
    @Override
    public Growth getGrowthById(Long id) throws BusinessException
    {
        try
        {
            return growthMapper.selectById(id);
        } catch (Exception e)
        {
            throw new BusinessException(500, "查询分类失败" + e.getMessage());
        }
    }

    //修改分类
    @Override
    public Integer updateGrowth(Growth growth) throws BusinessException
    {
        try
        {
            return growthMapper.updateById(growth);
        } catch (Exception e)
        {
            throw new BusinessException(500, "修改分类失败" + e.getMessage());
        }
    }

    //删除分类
    @Override
    public String deleteGrowth(Long id) throws BusinessException
    {
        try
        {
            int result = growthMapper.deleteById(id);
            if(result <= 0)
            {
                throw BusinessException.operateFail("删除分类失败");
            }
            List<Node> nodes = nodeMapper.listNodesByGrowthId(id);
            for (Node node : nodes)
            {
                nodeMapper.deleteById(node.getId());
            }
            return "修改成功";
        } catch (Exception e)
        {
            throw new BusinessException(500, "删除分类失败" + e.getMessage());
        }
    }

    //分页查询成长分类
    @Override
    public List<Growth> listGrowthPage(int pageNum, int pageSize)
    {
        try
        {
            Page<Growth> pageParam = new Page<>(pageNum, pageSize);
            IPage<Growth> result = growthMapper.selectPageList(pageParam);
            return result.getRecords();
        } catch(Exception e)
        {
            throw new BusinessException(500, "分页查询列表失败" + e.getMessage());
        }
    }


}
