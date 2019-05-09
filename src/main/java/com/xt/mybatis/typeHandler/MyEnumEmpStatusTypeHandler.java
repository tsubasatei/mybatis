package com.xt.mybatis.typeHandler;

import com.xt.mybatis.bean.Status;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 1、实现TypeHandler接口。或者继承BaseTypeHandler
 *
 */
public class MyEnumEmpStatusTypeHandler implements TypeHandler<Status> {
    /**
     * 定义当前数据如何保存到数据库中
     */
    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, Status status, JdbcType jdbcType) throws SQLException {
        System.out.println("要保存的状态码：" + status.getCode());
        preparedStatement.setString(i, status.getCode() + "");
    }

    @Override
    public Status getResult(ResultSet resultSet, String s) throws SQLException {
        //需要根据从数据库中拿到的枚举的状态码返回一个枚举对象
        return Status.getStatus(Integer.parseInt(resultSet.getString(s)));
    }

    @Override
    public Status getResult(ResultSet resultSet, int i) throws SQLException {
        return Status.getStatus(Integer.parseInt(resultSet.getString(i)));
    }

    @Override
    public Status getResult(CallableStatement callableStatement, int i) throws SQLException {
        return Status.getStatus(Integer.parseInt(callableStatement.getString(i)));
    }
}
