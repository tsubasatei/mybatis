package com.xt.mybatis.bean;

/**
 * @author xt
 * @create 2019/5/9 7:28
 * @Desc
 */
public enum Status {
    LOGIN(100, "登录"), LOGOUT(200, "登出"), REMOVED(300, "移除");

    private Integer code;
    private String msg;

    Status(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static Status getStatus(Integer code) {
        Status[] statuses = Status.values();
        for (Status status : statuses) {
            if (status.getCode() == code) {
                return status;
            }
        }
        return LOGOUT;
    }
}
