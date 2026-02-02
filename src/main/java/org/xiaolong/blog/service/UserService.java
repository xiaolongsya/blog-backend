package org.xiaolong.blog.service;

import org.xiaolong.blog.common.BusinessException;

public interface UserService
{
    String login(String username, String password) throws BusinessException;
}
