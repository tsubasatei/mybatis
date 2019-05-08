package com.xt.mybatis.mapper;

import com.xt.mybatis.bean.Employee;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author xt
 * @create 2019/5/3 15:48
 * @Desc
 */
public interface EmployeeMapper {
    //多条记录封装一个map：Map<Integer,Employee>:键是这条记录的主键，值是记录封装后的javaBean
    //@MapKey:告诉mybatis封装这个map的时候使用哪个属性作为map的key
    @MapKey("id")
    Map<Integer, Employee> getEmpByLastNameLikeReturnMap(String lastName);
    //返回一条记录的map；key就是列名，值就是对应的值
    List<Employee> getEmpsByLastNameLike(String lastName);
    Map<String, Object> getEmpByIdReturnMap(Integer id);

    Employee getEmpByMap(Map<String, Object> map);
    Employee getEmpByIdAndLastName(@Param("id") Integer id, @Param("lastName") String lastName);

    Employee getById(Integer id);
    Long addEmp(Employee employee);
    Boolean updateEmp(Employee employee);
    void deleteEmp(Integer id);

    List<Employee> selectAll();
}
