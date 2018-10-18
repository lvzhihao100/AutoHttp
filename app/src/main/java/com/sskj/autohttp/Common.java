package com.sskj.autohttp;

import com.sskj.libannotation.base.AutoHttpConfig;

/**
 * 作者 :吕志豪
 * 简书：https://www.jianshu.com/u/6e525b929aac
 * github：https://github.com/lvzhihao100
 * 描述：
 * 创建时间：2018-09-13 15:07
 */
@AutoHttpConfig(baseUrl = "http://www.adqki.cn",
        moduleName = "",
        ftlPath = "/home/lv/AndroidStudioProjects/AutoHttp/ftl",
        beanPath = "com.sskj.autohttp.bean",
        servicePath = "com.sskj.autohttp.http",
        presenterPath = "com.sskj.autohttp.presenter",
        iserviceClassName = "IService",
        serviceClassName = "HttpUtil",
        httpConfigClassName = "HttpConfig")
public class Common {
}
