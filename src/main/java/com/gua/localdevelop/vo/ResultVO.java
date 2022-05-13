package com.gua.localdevelop.vo;

import com.gua.localdevelop.contants.ErrorCode;
import org.springframework.util.ObjectUtils;

public class ResultVO<T> {
    private Integer errno;
    private String errmsg;
    private T data;




    public ResultVO(T data) {
        this.errno = ErrorCode.SUCCESS.getCode();
        this.errmsg = ErrorCode.SUCCESS.getDesc();
        this.data = data;
    }

    public ResultVO(int errno, String errmsg) {
        this.errno = errno;
        this.errmsg = errmsg;
    }

    public ResultVO(int errno, String errmsg, T data) {
        this.errno = errno;
        this.errmsg = errmsg;
        this.data = data;
    }

    public static <T> ResultVO<T> success() {
        return new ResultVO(null);
    }


    public static <T> ResultVO<T> success(T data) {
        return new ResultVO(data);
    }

    public static <T> ResultVO<T> error(ErrorCode error) {
        return new ResultVO(error.getCode(), error.getDesc());
    }

    public static <T> ResultVO<T> error(ErrorCode error, String errorDetail) {
        String msg = error.getDesc();
        if (!ObjectUtils.isEmpty(errorDetail)) {
            msg = msg + ": " + errorDetail;
        }

        return new ResultVO(error.getCode(), msg);
    }

    public static <T> ResultVO<T> error(ErrorCode error, T data) {
        return new ResultVO(error.getCode(), error.getDesc(), data);
    }

    public String toString() {
        return "Result(errno=" + this.getErrno() + ", errmsg=" + this.getErrmsg() + ", data=" + this.getData() +  ")";
    }

    public Integer getErrno() {
        return this.errno;
    }

    public void setErrno(Integer errno) {
        this.errno = errno;
    }

    public String getErrmsg() {
        return this.errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
