package org.xiaolong.blog.service.iml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xiaolong.blog.common.BusinessException;
import org.xiaolong.blog.entity.Recent;
import org.xiaolong.blog.mapper.RecentMapper;
import org.xiaolong.blog.service.RecentService;

import java.util.List;

@Service
public class RecentServiceIml implements RecentService
{
    @Autowired
    private RecentMapper recentMapper;

    //获取最近列表
    @Override
    public List<Recent> listRecent()
    {
        try
        {
            return recentMapper.selectList(null);
        } catch (Exception e)
        {
            throw new BusinessException(500, "查询最近列表失败" + e.getMessage());
        }
    }

    //上传最近事项
    @Override
    public String uploadRecent(Recent recent)
    {
        try
        {
            recentMapper.insert(recent);
            return "上传成功";
        } catch (Exception e)
        {
            throw new BusinessException(500, "上传最近事项失败" + e.getMessage());
        }
    }

    //删除最近事项
    @Override
    public String deleteRecent(Long id)
    {
        try
        {
            recentMapper.deleteById(id);
            return "删除成功";
        } catch (Exception e)
        {
            throw new BusinessException(500, "删除最近事项失败" + e.getMessage());
        }
    }

}
