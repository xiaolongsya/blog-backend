package org.xiaolong.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@TableName("dragon_node")
public class Node
{
    @TableId(type = IdType.AUTO)
    @Schema(description = "id")
    private Long id;

    @Schema(description = "分类id")
    private Long growthId;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "图片url")
    private String imgUrls;

    @Schema(description = "创建时间")
    private String createTime;

}
