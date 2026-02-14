package org.xiaolong.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("dragon_stack")
public class Stack
{
    @TableId(type = IdType.AUTO)
    @Schema(description = "id")
    private Long id;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "图片url")
    private String imgUrl;

    @Schema(description = "类型")
    private String type;

    @Schema(description = "星级评分1-5")
    private Integer starRating;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
