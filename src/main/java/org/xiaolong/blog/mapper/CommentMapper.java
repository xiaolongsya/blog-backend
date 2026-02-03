package org.xiaolong.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.xiaolong.blog.entity.Comment;

import java.time.LocalDateTime;
import java.util.Date;

@Mapper
public interface CommentMapper extends BaseMapper<Comment>
{

    // 统计某IP在指定时间段内的评论数
    @Select("select count(*) from dragon_comment where ip = #{ip} and create_time between #{startTime} and #{endTime}")
    Integer countCommentByIpAndTimeRange(
            @Param("ip") String ip,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
}
