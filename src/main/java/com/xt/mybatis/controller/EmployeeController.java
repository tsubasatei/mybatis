package com.xt.mybatis.controller;

import com.xt.mybatis.bean.Employee;
import com.xt.mybatis.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

/**
 * @author xt
 * @create 2019/5/8 10:34
 * @Desc
 */
@Controller
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @GetMapping("getemps")
    public String list(Map<String, Object> map) {
        List<Employee> emps = employeeService.findList();
        map.put("emps", emps);
        return "list";
    }
}
