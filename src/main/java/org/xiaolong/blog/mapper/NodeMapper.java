package org.xiaolong.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.xiaolong.blog.entity.Node;

import java.util.List;

@Mapper
public interface NodeMapper extends BaseMapper<Node>
{
    //根据分类id查询节点
    @Select("select * from dragon_node where growth_id = #{growth_id}")
    List<Node> listNodesByGrowthId(@Param("growth_id") Long growth_id);

    //根据分类id分页查询节点
    @Select("select * from dragon_node where growth_id = #{growth_id}")
    IPage<Node> listNodesByPage(@Param("page") IPage<Node> page, @Param("growth_id") Long growthId);
}
