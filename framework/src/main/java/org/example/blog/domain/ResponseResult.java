package org.example.blog.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseResult<T> implements Serializable {
    private Integer code;
    private String msg;
    private T data;
    public ResponseResult() {
        this.code = AppHttpCodeEnum.SUCCESS.getCode();
        this.msg = AppHttpCodeEnum.SUCCESS.getMsg();
    }
    public ResponseResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public static ResponseResult okResult() {
        ResponseResult result = new ResponseResult();
        return result;
    }
    public static ResponseResult okResult(Object data) {
        ResponseResult result = new ResponseResult();
        if (data != null) {
            result.setData(data);
        }
        return result;
    }
    public enum AppHttpCodeEnum {
        SUCCESS(200, "操作成功"),
        NEED_LOGIN(401, "需要登录后操作"),
        NO_OPERATOR_AUTH(403, "无权限操作"),
        SYSTEM_ERROR(500, "出现错误"),
        USERNAME_EXIST(501, "用户名已存在"),
        PHONENUMBER_EXIST(502, "手机号已存在"),
        EMAIL_EXIST(503, "邮箱已存在"),
        REQUIRE_USERNAME(504, "必需填写用户名"),
        LOGIN_ERROR(505, "用户名或密码错误");
        int code;
        String msg;
        AppHttpCodeEnum(int code, String errorMessage) {
            this.code = code;
            this.msg = errorMessage;
        }
        public int getCode() {
            return code;
        }
        public String getMsg() {
            return msg;
        }
    }
    public static ResponseResult errorResult(int code, String msg) {
        ResponseResult result = new ResponseResult(code, msg);
        return result;
    }
    public static ResponseResult errorResult(AppHttpCodeEnum enums) {
        ResponseResult result = new ResponseResult(enums.getCode(),
                enums.getMsg());
        return result;
    }
    public static ResponseResult errorResult(AppHttpCodeEnum enums, String
            msg) {
        ResponseResult result = new ResponseResult(enums.getCode(), msg);
        return result;
    }
    public Integer getCode() {return code;}
    public void setCode(Integer code) {this.code = code;}
    public String getMsg() {return msg;}
    public void setMsg(String msg) {this.msg = msg;}
    public T getData() {return data;}
    public void setData(T data) {this.data = data;}
}
