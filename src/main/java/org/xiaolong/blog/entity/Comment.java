package org.xiaolong.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("dragon_comment")
public class Comment
{
    @TableId(type = IdType.AUTO)
    @Schema(description = "评论id")
    private Long id;

    @Schema(description = "评论者名称")
    private String name;

    @Schema(description = "评论内容")
    private String content;

    @Schema(description = "评论时间")
    private LocalDateTime createTime;

    @Schema(description = "联系方式")
    private String contact;

    @Schema(description = "临时，中奖情况")
    private String status;

    @Schema(description = "评论者ip")
    private String ip;

    @Schema(description = "回复")
    private String reply;


}
