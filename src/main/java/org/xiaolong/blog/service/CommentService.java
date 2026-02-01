package org.xiaolong.blog.service;

import org.xiaolong.blog.common.BusinessException;
import org.xiaolong.blog.entity.Comment;

import java.util.List;

public interface CommentService
{
    //按时间倒序查询所有评论
    List<Comment> listCommentsByTimeDesc() throws BusinessException;
    //上传评论
    Long uploadComment(Comment comment) throws BusinessException;
}
