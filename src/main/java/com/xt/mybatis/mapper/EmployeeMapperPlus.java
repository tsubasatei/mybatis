package com.xt.mybatis.mapper;

import com.xt.mybatis.bean.Employee;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface EmployeeMapperPlus {
	
	Employee getEmpById(Integer id);
	
	Employee getEmpAndDept(Integer id);

	Employee getEmpByIdStep(Integer id);

	List<Employee> getEmpsByDeptId(Integer deptId);


	// 动态SQL
	List<Employee> getEmpsTestInnerParameter(Employee employee);

	//携带了哪个字段查询条件就带上这个字段的值
	List<Employee> getEmpsByConditionIf(Employee employee);

	List<Employee> getEmpsByConditionTrim(Employee employee);

	List<Employee> getEmpsByConditionChoose(Employee employee);

	void updateEmp(Employee employee);

	//查询员工id'在给定集合中的
	List<Employee> getEmpsByConditionForeach(@Param("ids")List<Integer> ids);

	void addEmps(@Param("emps")List<Employee> emps);

}
