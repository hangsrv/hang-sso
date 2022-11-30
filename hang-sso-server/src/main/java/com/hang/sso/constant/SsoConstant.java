package com.hang.sso.constant;

public class SsoConstant {
    public static final String REDIRECT_URI = "redirectUri";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String ST_REQ_PREFIX = "st";
    public static final String TOKEN_PREFIX = "token";
    public static final long CODE_EXPIRE = 60 * 1000;
    public static final long TOKEN_EXPIRE = 7 * 24 * 60 * 60 * 1000;
    //{Seconds} {Minutes} {Hours} {DayOfMonth} {Month} {DayOfWeek} {Year}
    public static final String CODE_SCHEDULED_CRON = "*/5 * * * * ?";
    public static final String TOKEN_SCHEDULED_CRON = "0 0 1 * * ?";

}
