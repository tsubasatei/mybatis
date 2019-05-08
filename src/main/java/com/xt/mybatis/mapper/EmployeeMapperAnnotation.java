package com.xt.mybatis.mapper;

import com.xt.mybatis.bean.Employee;
import org.apache.ibatis.annotations.Select;

/**
 * @author xt
 * @create 2019/5/3 15:48
 * @Desc
 */
public interface EmployeeMapperAnnotation {

    @Select(value = {"select * from employee where id = #{id}"})
    Employee getById(Integer id);
}

