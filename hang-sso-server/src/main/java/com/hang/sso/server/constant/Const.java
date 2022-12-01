package com.hang.sso.server.constant;

public class Const {
    public static final String REDIRECT_URI = "redirectUri";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String ST = "st";
    public static final String TOKEN = "token";
    public static final String LOGOUT_URL = "logoutUrl";
    public static final long CODE_EXPIRE = 60 * 1000;
    public static final int COOKIE_EXPIRE = 7 * 24 * 60 * 60;
    public static final long TOKEN_EXPIRE = 7 * 24 * 60 * 60 * 1000;
    //{Seconds} {Minutes} {Hours} {DayOfMonth} {Month} {DayOfWeek} {Year}
    public static final String CODE_SCHEDULED_CRON = "*/5 * * * * ?";
    public static final String TOKEN_SCHEDULED_CRON = "0 0 1 * * ?";
    public static final String OK = "ok";
    public static final String ERR = "err";
}
