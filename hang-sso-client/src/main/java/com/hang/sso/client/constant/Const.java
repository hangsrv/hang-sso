package com.hang.sso.client.constant;

public class Const {
    public static final String LOGIN_URL = "/sso/toLogin";
    public static final String TOKEN_REFRESH = "/sso/refresh";
    public static final String INDEX_URL = "/web/index";
    public static final String REDIRECT_URI = "redirectUri";
    public static final String SESSION_NAME = "token";
    public static final String OK = "ok";
    public static final String ERR = "err";
    public static final int TOKEN_EXPIRE = 24 * 60 * 60;
    public static final String REGEXP_URL = "((http[s]?)?(://))?([^/]*)(/?.*)";
    public static final String REGEXP_REPLACE_URL = "$2$3$4";
}
