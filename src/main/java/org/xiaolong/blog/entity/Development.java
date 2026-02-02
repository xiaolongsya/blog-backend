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
@TableName("dragon_development")
public class Development
{
    @TableId(type = IdType.AUTO)
    @Schema(description = "更新id")
    private Long id;
    @Schema(description = "更新内容")
    private String content;
    @TableField(typeHandler = ListStringTypeHandler.class, jdbcType = JdbcType.VARCHAR)
    @Schema(description = "图片urls,接受前端")
    private List<String> imgUrls;
    @Schema(description = "更新时间")
    private String createTime;


}
