package com.hang.sso.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    public static final int SUCCESS_CODE = 1;
    public static final int ERROR_CODE = 9999;

    private int code;
    private String message;
    private T data;

    public static <T> Result<T> success() {
        return success(null, null);
    }

    public static <T> Result<T> success(String message) {
        return success(message, null);
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<>(SUCCESS_CODE, message, data);
    }


    public static <T> Result<T> error(String message) {
        return error(message, null);
    }

    public static <T> Result<T> error(String message, T data) {
        return new Result<>(ERROR_CODE, message, data);
    }

    public boolean isSuccess() {
        return this.code == SUCCESS_CODE;
    }
}
