package org.xiaolong.blog.service.iml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xiaolong.blog.common.BusinessException;
import org.xiaolong.blog.entity.User;
import org.xiaolong.blog.mapper.UserMapper;
import org.xiaolong.blog.service.UserService;

@Service
public class UserServiceIml implements UserService
{
    @Autowired
    private UserMapper userMapper;
    //登录实现
    @Override
    public String login(String username, String password) throws BusinessException
    {
        try
        {
            User user = userMapper.findByUsername(username);
            if (user == null)
            {
                throw BusinessException.paramError("用户不存在");
            }
            if (!user.getPassword().equals(password))
            {
                throw BusinessException.paramError("密码错误");
            }
            return("登陆成功");
        } catch (Exception e)
        {
            throw new BusinessException(500, "登录失败" + e.getMessage());
        }
    }
}
