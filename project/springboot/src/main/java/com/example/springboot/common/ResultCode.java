package com.example.springboot.common;

public enum ResultCode {
    SUCCESS("0", "Success"),
    ERROR("-1", "System Error"),
    PARAM_ERROR("1001", "Parameter Error"),
    USER_EXIST_ERROR("2001", "Username Already Exists"),
    USER_NOT_LOGIN("2001", "User Not Logged In"),
    USER_ACCOUNT_ERROR("2002", "Incorrect Username or Password"),
    USER_NOT_EXIST_ERROR("2003", "User Does Not Exist"),
    PARAM_LOST_ERROR("2004", "Missing Parameter"),
    PARAM_PASSWORD_ERROR("2005", "Incorrect Original Password"),
    TOKEN_INVALID_ERROR("401", "Invalid Token"),
    TOKEN_CHECK_ERROR("401", "Token Verification Failed, Please Login Again"),
    DATA_LESS("402", "Administrator Data Less Than 5"),
    USERNAME_ISNULL("406", "Username Is Empty"),
    TITLE_ISNULL("402","Title Is Empty"),
    ;

    public String code;
    public String msg;

    ResultCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
