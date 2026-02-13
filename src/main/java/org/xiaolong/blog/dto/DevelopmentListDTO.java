package org.xiaolong.blog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.xiaolong.blog.entity.Development;

import java.util.List;

@Data
@AllArgsConstructor
@Schema(description = "更新列表")
public class DevelopmentListDTO
{
    @Schema(description = "更新总数")
    private Long total;
    @Schema(description = "更新列表")
    private List<Development> list;
}
