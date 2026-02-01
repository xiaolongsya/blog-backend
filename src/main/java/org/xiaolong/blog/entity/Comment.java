package org.xiaolong.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

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
    private String createTime;


}
