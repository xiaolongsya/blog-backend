package org.xiaolong.blog.service;

import org.xiaolong.blog.common.BusinessException;
import org.xiaolong.blog.entity.Growth;

import java.util.List;

public interface GrowthService
{
    List<Growth> listGrowth() throws BusinessException;
}
