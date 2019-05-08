package com.xt.mybatis.bean;

import lombok.*;

import java.util.List;

/**
 * @author xt
 * @create 2019/5/7 9:58
 * @Desc
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Department {
    private Integer id;
    private String deptName;

    private List<Employee> emps;

}
