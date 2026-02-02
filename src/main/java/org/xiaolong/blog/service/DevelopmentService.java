package org.xiaolong.blog.service;

import org.xiaolong.blog.common.BusinessException;
import org.xiaolong.blog.entity.Development;

import java.util.List;

public interface DevelopmentService
{
    //上传更新记录
    Long upload(Development development) throws BusinessException;
    //按时间倒序查询所有更新记录
    List<Development> list() throws BusinessException;
}
