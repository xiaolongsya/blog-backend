package org.xiaolong.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.xiaolong.blog.entity.Growth;

import java.util.List;

@Mapper
public interface GrowthMapper extends BaseMapper<Growth>
{
    //分页查询成长分类
    @Select("select * from dragon_growth")
    IPage<Growth> selectPageList(@Param("page") IPage<Growth> page);
}
