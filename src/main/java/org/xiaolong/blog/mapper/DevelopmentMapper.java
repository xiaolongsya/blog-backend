package org.xiaolong.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.xiaolong.blog.entity.Development;

import java.util.List;

@Mapper
public interface DevelopmentMapper extends BaseMapper<Development>
{
    //分页查询倒序
    @Select("select * from dragon_development order by create_time desc")
    IPage<Development> selectPageList(IPage<Development> page);
}
