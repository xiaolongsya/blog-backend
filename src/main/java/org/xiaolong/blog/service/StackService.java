package org.xiaolong.blog.service;

import org.xiaolong.blog.common.BusinessException;
import org.xiaolong.blog.entity.Stack;

import java.util.List;

public interface StackService
{
    //上传技术栈
    Long uploadStack(Stack stack) throws BusinessException;
    //获取所有技术栈
    List<Stack> listStacks() throws BusinessException;
}
