package org.xiaolong.blog.service.iml;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xiaolong.blog.common.BusinessException;
import org.xiaolong.blog.entity.Development;
import org.xiaolong.blog.mapper.DevelopmentMapper;
import org.xiaolong.blog.service.DevelopmentService;

import java.util.List;

@Service
public class DevelopmentServiceIml implements DevelopmentService
{
    @Autowired
    private DevelopmentMapper developmentMapper;
    //上传更新记录
    @Override
    public Long upload(Development development) throws BusinessException
    {

        if(development.getContent() == null || development.getContent().trim().isEmpty())
        {
            throw new BusinessException(400, "内容不能为空");
        }

        try
        {
            int result = developmentMapper.insert(development);
            if (result <= 0)
            {
                throw new BusinessException(400, "上传失败");
            }
            return development.getId();
        } catch (BusinessException e)
        {
            throw new BusinessException(500, "更新记录上传失败" + e.getMessage());
        }
    }

    //查询更新记录,按照时间倒序
    @Override
    public List<Development> list() throws BusinessException
    {
        try
        {
            QueryWrapper<Development> queryWrapper = new QueryWrapper<>();
            queryWrapper.orderByDesc("create_time");
            return developmentMapper.selectList(queryWrapper);
        } catch (Exception e)
        {
            throw new BusinessException(500, "查询列表失败" + e.getMessage());
        }
    }


}
