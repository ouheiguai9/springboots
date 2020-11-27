package com.byakuya.boot.factory;

/**
 * Created by ganzl on 2020/4/2.
 * 常量工具类,用于定义系统使用的所有常量
 */
public class ConstantUtils {
    //管理员用户特有权限
    public static final String ADMIN_USER_AUTHORITY = "ADMIN";
    //验证码在请求中的参数名
    public static final String CAPTCHA_PARAMETER_KEY = "_captcha_";
    //验证码在session中的键名
    public static final String SESSION_CAPTCHA_KEY = "_session_captcha_";

    private ConstantUtils() {

    }
}
