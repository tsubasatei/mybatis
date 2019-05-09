package com.xt.mybatis.service;

import com.xt.mybatis.bean.Employee;
import com.xt.mybatis.mapper.EmployeeMapper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xt
 * @create 2019/5/8 10:36
 * @Desc
 */
@Service
public class EmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;
    
    @Autowired
    private SqlSession sqlSession; // 批量操作

    public List<Employee> findList() {
//        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
        return employeeMapper.selectAll();
    }
}
