package org.xiaolong.blog.service;

import org.xiaolong.blog.common.BusinessException;
import org.xiaolong.blog.entity.Reply;

public interface ReplyService
{

    Long addReply(Reply reply) throws BusinessException;
}
