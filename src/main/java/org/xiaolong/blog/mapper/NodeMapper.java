package org.xiaolong.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.xiaolong.blog.dto.GrowthSearchDTO;
import org.xiaolong.blog.entity.Node;

import java.util.List;
import java.util.Map;

@Mapper
public interface NodeMapper extends BaseMapper<Node>
{
    //根据分类id查询节点
    @Select("select * from dragon_node where growth_id = #{growth_id}")
    List<Node> listNodesByGrowthId(@Param("growth_id") Long growth_id);

    //根据分类id分页查询节点
    @Select("select * from dragon_node where growth_id = #{growth_id}")
    IPage<Node> listNodesByPage(@Param("page") IPage<Node> page, @Param("growth_id") Long growthId);

    /**
     * 搜索功能：传入关键字，返回匹配的节点
     * 1. 关联查询 dragon_growth 表
     * 2. 同时匹配 标题(title，在dragon_growth里面) 或 内容(content在dragon_node里面)
     */
    @Select("SELECT n.id, " +
            "       n.content, " +
            "       g.name as title, " +
            "       n.create_time as createTime, " +
            "       g.id as growthId " + // <--- 新增这行：查出 growth 表的主键并映射为 DTO 的 growthId
            "FROM dragon_node n " +
            "LEFT JOIN dragon_growth g ON n.growth_id = g.id " +
            "WHERE g.name LIKE CONCAT('%', #{keyword}, '%') " +
            "   OR n.content LIKE CONCAT('%', #{keyword}, '%') " +
            "ORDER BY n.create_time DESC")
    List<GrowthSearchDTO> searchByKeyword(@Param("keyword") String keyword);
}
