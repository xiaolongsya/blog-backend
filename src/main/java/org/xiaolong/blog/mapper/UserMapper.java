package org.xiaolong.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.xiaolong.blog.entity.User;

@Mapper
public interface UserMapper extends BaseMapper<User>
{
    //根据用户名查询用户
    @Select( "select * from dragon_user where username = #{username}")
    User findByUsername(String username);
}
