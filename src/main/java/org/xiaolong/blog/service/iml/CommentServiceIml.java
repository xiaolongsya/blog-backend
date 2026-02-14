package org.xiaolong.blog.service.iml;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xiaolong.blog.common.BusinessException;
import org.xiaolong.blog.dto.CommentListDTO;
import org.xiaolong.blog.entity.Comment;
import org.xiaolong.blog.mapper.CommentMapper;
import org.xiaolong.blog.mapper.ReplyMapper;
import org.xiaolong.blog.service.CommentService;
import org.xiaolong.blog.utils.IpUtils;
import org.xiaolong.blog.utils.TimeUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceIml implements CommentService
{
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private ReplyMapper replyMapper;


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

    @Override
    public Long uploadComment(Comment comment, HttpServletRequest request) throws BusinessException {
        try {
            // 1. 参数合法性校验（保留，非常必要）
            if (comment.getName() == null || comment.getName().trim().isEmpty()) {
                throw new BusinessException(400, "昵称不能为空");
            }
            if (comment.getName().length() > 40) {
                throw new BusinessException(400, "昵称不能超过40个字符");
            }
            if (comment.getContent() == null || comment.getContent().trim().isEmpty()) {
                throw new BusinessException(400, "内容不能为空");
            }
            if (comment.getContent().length() > 400) {
                throw new BusinessException(400, "内容不能超过400个字符");
            }

            // 2. IP与时间赋值（保留，因为存入数据库需要记录这些信息）
            String realIp = IpUtils.getRealIp(request);
            if (realIp == null || realIp.trim().isEmpty()) {
                throw new BusinessException(400, "无法获取您的IP地址，无法提交评论");
            }
            comment.setIp(realIp);
            comment.setCreateTime(LocalDateTime.now(TimeUtils.CHINA_ZONE).truncatedTo(ChronoUnit.MILLIS));

            // =====================================================================
            // 核心改动：删除了原来的第 3、4、5 步（基于数据库的时间范围查询和超限判断）
            // 因为如果代码能走到这里，说明拦截器已经放行了，绝对没有超限！
            // =====================================================================

            // 3. 插入评论
            int result = commentMapper.insert(comment);
            if (result <= 0) {
                throw new BusinessException(400, "上传失败");
            }

            return comment.getId();
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(500, "服务器内部错误：" + e.getMessage());
        }
    }

    //分页查询评论
    @Override
    public CommentListDTO listComments(Integer pageNum, Integer pageSize) throws BusinessException
    {
        try
        {
            Page<Comment> pageParam = new Page<>(pageNum, pageSize);
            IPage<Comment> result = commentMapper.selectPage(pageParam);
            return new CommentListDTO(result.getTotal(), result.getRecords(), replyMapper.selectList(null));
        } catch (Exception e)
        {
            throw new BusinessException(500, "分页查询列表失败" + e.getMessage());
        }
    }
}
