package org.xiaolong.blog.service.iml;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xiaolong.blog.common.BusinessException;
import org.xiaolong.blog.entity.Comment;
import org.xiaolong.blog.mapper.CommentMapper;
import org.xiaolong.blog.service.CommentService;

import java.util.List;

@Service
public class CommentServiceIml implements CommentService
{
    @Autowired
    private CommentMapper commentMapper;

    // 查询所有评论
    @Override
    public List<Comment> listCommentsByTimeDesc() throws BusinessException
    {
        try
        {
            // 构造查询条件：按createTime倒序排序
            QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
            queryWrapper.orderByDesc("create_time"); // 注意字段名要和实体类一致（你的实体类中是creatTime）
            return commentMapper.selectList(queryWrapper);
        } catch (Exception e)
        {
            throw new BusinessException(500, "查询列表失败" + e.getMessage());
        }
    }

    //上传评论
    @Override
    public Long uploadComment(Comment comment) throws BusinessException
    {
        try
        {
            int result = commentMapper.insert(comment);
            if (result <= 0)
            {
                throw new BusinessException(400, "上传失败");
            }
            return comment.getId();
        } catch (BusinessException e)
        {
            throw new BusinessException(500, "服务器内部错误");
        }
    }
}
