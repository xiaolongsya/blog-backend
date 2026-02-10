package org.xiaolong.blog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "搜索结果")
public class GrowthSearchDTO
{
    private Long growthId;
    private String title;     // 这里的标题在分类表里
    private String content;   // 这里的内容在节点表里
    private LocalDateTime createTime;
}
