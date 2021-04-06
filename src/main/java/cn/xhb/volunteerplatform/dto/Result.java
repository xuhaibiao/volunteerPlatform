package cn.xhb.volunteerplatform.dto;

import lombok.Data;

/**
 * @author HaibiaoXu
 * @date Create in 11:10 2021/3/4
 * @modified By
 */
@Data
public class Result<T> {
    /**
     * 状态码
     * 1：成功
     * 2：失败
     */
    int code;
    /**
     * 消息
     */
    String msg;
    /**
     * 数据
     */
    T data;

    /**
     * 有需要时用来做标识区分
     */
    String flag;

    private Result(int code, String msg, T data,String flag) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.flag = flag;
    }

    public static <T> Result<T> success(String msg, T data) {
        return new Result<>(1, msg, data,"");
    }
    public static <T> Result<T> success(T data) {
        return new Result<>(1, "", data,"");
    }
    public static <T> Result<T> success(T data,String flag) {
        return new Result<>(1, "", data,flag);
    }
    public static <T> Result<T> error(String msg) {
        return new Result<>(2, msg, null,"");
    }
    public static <T> Result<T> error() {
        return new Result<>(2, "", null,"");
    }


}
