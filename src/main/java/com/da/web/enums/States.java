package com.da.web.enums;

/**
 * 状态码
 */
public enum States {
    //    成功的状态码
    OK(200),
    //    失败的状态码
    ERROR(500),
    //    找不到的状态码
    NOT_FOUND(404);
    private final int code;

    @Override
    public String toString() {
        return String.valueOf(code);
    }

    States(int code) {
        this.code = code;
    }

}
