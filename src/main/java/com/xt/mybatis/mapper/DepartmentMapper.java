package com.xt.mybatis.mapper;

import com.xt.mybatis.bean.Department;

/**
 * @author xt
 * @create 2019/5/3 15:48
 * @Desc
 */
public interface DepartmentMapper {
    Department getDeptById(Integer id);

    Department getDeptByIdPlus(Integer id);

    Department getDeptByIdStep(Integer id);
}
