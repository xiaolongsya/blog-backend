package org.xiaolong.blog.service.iml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xiaolong.blog.common.BusinessException;
import org.xiaolong.blog.entity.Reply;
import org.xiaolong.blog.mapper.ReplyMapper;
import org.xiaolong.blog.service.ReplyService;

@Service
public class ReplyServiceIml implements ReplyService
{
    @Autowired
    private ReplyMapper replyMapper;
    //上传回复,返回回复id
    public Long addReply(Reply reply) throws BusinessException
    {
        try
        {
            int result = replyMapper.insert(reply);
            if (result <= 0)
            {
                throw new BusinessException(400, "上传失败");
            }
            return reply.getId();
        } catch (BusinessException e)
        {
            throw new BusinessException(400, "上传失败" + e.getMessage());
        }
    }
}
