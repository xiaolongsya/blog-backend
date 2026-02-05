package org.xiaolong.blog.service;

import org.xiaolong.blog.common.BusinessException;
import org.xiaolong.blog.entity.Growth;

import java.util.List;

public interface GrowthService
{
    //获取分类列表
    List<Growth> listGrowth() throws BusinessException;
    //上传分类
    String uploadGrowth(Growth growth) throws BusinessException;
    //根据id查询分类
    Growth getGrowthById(Long id) throws BusinessException;
    //修改分类
    Integer updateGrowth(Growth growth) throws BusinessException;

    String deleteGrowth(Long id) throws BusinessException;
}
