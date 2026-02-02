package org.xiaolong.blog.service.iml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xiaolong.blog.common.BusinessException;
import org.xiaolong.blog.entity.Growth;
import org.xiaolong.blog.mapper.GrowthMapper;
import org.xiaolong.blog.service.GrowthService;

import java.util.List;

@Service
public class GrowthServiceIml implements GrowthService
{
    @Autowired
    private GrowthMapper growthMapper;
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
}
