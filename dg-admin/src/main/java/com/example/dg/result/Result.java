package com.example.dg.result;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author xinxin
 * @version 1.0
 * @Date 2024/12/5 17:34
 */
@Data
public class Result<T> extends HashMap<String, Object> implements Serializable {
    private Integer code; //编码：1访问成功，2游客，3管理员，4超级管理员，0和其它数字为失败
    private String msg; //错误信息
    private T data; //数据

    public static <T> Result<T> success() {
        Result<T> result = new Result<T>();
        result.code = 1;
        result.put("code", 1);
        result.put("msg", "访问成功");
        return result;
    }

    public static <T> Result<T> success(T object) {
        Result<T> result = new Result<T>();
        result.data = object;
        result.code = 1;
        return result;
    }

    public static <T> Result<T> success(Integer code) {
        Result<T> result = new Result<T>();
        result.code = code;
        return result;
    }

    public static <T> Result<T> error(String msg) {
        Result result = new Result();
        result.put("code", 0);
        result.put("msg", msg);
        return result;
    }

    public static Result error(int code, String msg)
    {
        Result result = new Result();
        result.put("code", code);
        result.put("msg", msg);
        return result;
    }
    public static <T> Result<T> success(String msg) {
        Result result = new Result();
        result.put("code", 1);
        result.put("msg", msg);
        return result;
    }


}
