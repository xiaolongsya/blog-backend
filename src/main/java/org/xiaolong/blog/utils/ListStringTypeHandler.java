package org.xiaolong.blog.utils;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// 告诉 MyBatis 这个处理器处理 List 类型
@MappedTypes(List.class)
// 告诉 MyBatis 这个处理器处理数据库中的 VARCHAR 类型
@MappedJdbcTypes(JdbcType.VARCHAR)
public class ListStringTypeHandler extends BaseTypeHandler<List<String>> {


    // 写入数据库：List → String（逗号分隔）
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType) throws SQLException {
        String str = String.join(",", parameter);
        ps.setString(i, str);
    }

    // 读取数据库：String → List
    @Override
    public List<String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        System.out.println("调用了 getNullableResult，columnName: " + columnName);
        String str = rs.getString(columnName);
        return convertStr2List(str);
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String str = rs.getString(columnIndex);
        return convertStr2List(str);
    }

    @Override
    public List<String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String str = cs.getString(columnIndex);
        return convertStr2List(str);
    }

    // 辅助方法：字符串转 List
    private List<String> convertStr2List(String str) {
        if (str == null || str.trim ().isEmpty ()) {
// Java 8 兼容写法，返回空的 ArrayList
            return new ArrayList<>();
        }
        return Arrays.stream(str.split(","))
                .map(String::trim)
                .collect(Collectors.toList());
    }
}