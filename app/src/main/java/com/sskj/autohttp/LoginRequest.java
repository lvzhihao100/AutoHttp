package com.sskj.autohttp;

import com.sskj.libannotation.request.AutoRequestConfig;
import com.sskj.libannotation.request.AutoRequestParamConfig;

/**
 * 作者 :吕志豪
 * 简书：https://www.jianshu.com/u/6e525b929aac
 * github：https://github.com/lvzhihao100
 * 描述：
 * 创建时间：2018-09-17 20:05
 */
@AutoRequestConfig(methodType = "POST",
        modelName = "UserBean",
        path = "/user/login",
        methodName = "login",
        methodDesc = "登录",
        constantName = "LOGIN",
        isCreateModel = true,
        isNoHTTP = true,
        isCreatePresenter = true,
        presenterClassName = "LoginActivityPresenter"
)
public class LoginRequest {
    @AutoRequestParamConfig(value = "fjj23j2aa12iu22iux1jaxa3as", desc = "手机序列号")
    String machine;
    @AutoRequestParamConfig(value = "1", desc = "1 安卓 2 ios")
    String platform;
}
