package org.xiaolong.blog.service;

import org.xiaolong.blog.common.BusinessException;
import org.xiaolong.blog.entity.Node;

import java.util.List;

public interface NodeService
{

    List<Node> listNodesByGrowthId(Long growthId) throws BusinessException;
}
