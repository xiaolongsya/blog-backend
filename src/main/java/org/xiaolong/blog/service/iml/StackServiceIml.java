package org.xiaolong.blog.service.iml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xiaolong.blog.common.BusinessException;
import org.xiaolong.blog.entity.Stack;
import org.xiaolong.blog.interceptor.RateLimit;
import org.xiaolong.blog.mapper.StackMapper;
import org.xiaolong.blog.service.StackService;

import java.util.List;

@Service
public class StackServiceIml implements StackService
{
    @Autowired
    private StackMapper stackMapper;
    //上传技术栈,返回id
    @Override
    public Long uploadStack(Stack stack) throws BusinessException
    {
        try
        {
            int result = stackMapper.insert(stack);
            if (result <= 0)
            {
                throw BusinessException.operateFail("上传失败");
            }
            return stack.getId();
        } catch (BusinessException e)
        {
            throw new BusinessException(500, "上传失败" + e.getMessage());
        }
    }

    //获取所有技术栈

    public List<Stack> listStacks() throws BusinessException
    {
        try
        {
            return stackMapper.selectList(null);
        } catch (Exception e)
        {
            throw new BusinessException(500, "查询列表失败" + e.getMessage());
        }
    }
}
