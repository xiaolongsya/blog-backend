package org.xiaolong.blog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.xiaolong.blog.entity.Comment;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "评论列表")
public class CommentListDTO
{
    @Schema(description = "评论总数")
    private Long total;
    @Schema(description = "评论列表")
    private List<Comment> list;
}
