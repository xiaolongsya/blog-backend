package org.xiaolong.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.xiaolong.blog.entity.Comment;

@Mapper
public interface CommentMapper extends BaseMapper<Comment>
{

}
