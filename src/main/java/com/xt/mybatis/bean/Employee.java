package com.xt.mybatis.bean;

import lombok.*;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
//@Alias("emp")
public class Employee implements Serializable {

    private Integer id;
    private String lastName;
    private String email;
    private String gender;

    private Department dept;

    private Status status = Status.LOGIN;

    public Employee(Integer id, String lastName, String email, String gender) {
        this.id = id;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
    }

    public Employee(String lastName, String email, String gender) {
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
    }

    public Employee(Integer id, String lastName, String email, String gender, Status status) {
        this.id = id;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
        this.status = status;
    }
}
