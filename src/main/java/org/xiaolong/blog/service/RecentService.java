package org.xiaolong.blog.service;

import org.xiaolong.blog.common.BusinessException;
import org.xiaolong.blog.entity.Recent;

import java.util.List;

public interface RecentService
{
    List<Recent> listRecent() throws BusinessException;

    String uploadRecent(Recent recent) throws BusinessException;

    String deleteRecent(Long id) throws BusinessException;
}
