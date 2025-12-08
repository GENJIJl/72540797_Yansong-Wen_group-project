package com.example.springboot.common;

public class Result {
    //    静态属性
    private static final String SUCCESS = "0";
    private static final String ERROR = "-1";

    //    类的属性
    private String code;
    private String msg;
    private Object data;


    //    getter和setter方法
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    //    Custom methods
    public static Result success() {
//        Create a Result object
        Result result = new Result();
        result.setCode(SUCCESS); // Set the code value to SUCCESS
        return result;
    }

    public static Result success(Object data) {
        Result result = new Result();
//      Set code and data
        result.setCode(SUCCESS);
        result.setData(data);
        return result;
    }

    public static Result error(String msg) {
        Result result = new Result();
//        Set code value and error message
        result.setCode(ERROR);
        result.setMsg(msg);
        return result;
    }
}
