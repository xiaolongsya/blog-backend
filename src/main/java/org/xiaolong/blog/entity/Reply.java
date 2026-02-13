package org.xiaolong.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("dragon_comment_reply")
public class Reply
{
    @TableId(type = IdType.AUTO)
    @Schema(description = "回复id")
    private Long id;

    @Schema(description = "评论id")
    private Long commentId;

    @Schema(description = "名字")
    private String name;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "回复时间")
    private LocalDateTime createTime;

}
