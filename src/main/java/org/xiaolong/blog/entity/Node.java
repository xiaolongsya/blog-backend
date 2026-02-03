package org.xiaolong.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;
import org.xiaolong.blog.utils.ListStringTypeHandler;

import java.util.List;

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

    @TableField(typeHandler = ListStringTypeHandler.class, jdbcType = JdbcType.VARCHAR)
    @Schema(description = "图片url")
    private List<String> imgUrls;

    @Schema(description = "创建时间")
    private String createTime;

}
