package org.xiaolong.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@TableName("dragon_growth")
public class Growth
{
    @TableId(type = IdType.AUTO)
    @Schema(description = "id")
    private Long id;

    @Schema(description = "类名")
    private String name;

    @Schema(description = "节点数")
    private Long nodeCount;

    @Schema(description = "最近的节点id")
    private Long lastNode;

    @Schema(description = "类型")
    private String type;

    @Schema(description = "创建时间")
    private String createTime;
}
