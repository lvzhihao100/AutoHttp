package com.sskj.libcompiler;

/**
 * 作者 :吕志豪
 * 简书：https://www.jianshu.com/u/6e525b929aac
 * github：https://github.com/lvzhihao100
 * 描述：
 * 创建时间：2018-09-17 20:13
 */
public class HttpConfig {
    String moduleName;
    String beanPath;
    String servicePath;
    String presenterPath;
    String iserviceClassName;
    String serviceClassName;
    String httpConfigClassName;
    String ftlPath;
    String baseUrl;

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getHttpConfigClassName() {
        return httpConfigClassName;
    }

    public void setHttpConfigClassName(String httpConfigClassName) {
        this.httpConfigClassName = httpConfigClassName;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public HttpConfig setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public String getFtlPath() {
        return ftlPath;
    }

    public HttpConfig setFtlPath(String ftlPath) {
        this.ftlPath = ftlPath;
        return this;
    }

    public String getBeanPath() {
        return beanPath;
    }

    public HttpConfig setBeanPath(String beanPath) {
        this.beanPath = beanPath;
        return this;
    }

    public String getServicePath() {
        return servicePath;
    }

    public HttpConfig setServicePath(String servicePath) {
        this.servicePath = servicePath;
        return this;
    }

    public String getPresenterPath() {
        return presenterPath;
    }

    public HttpConfig setPresenterPath(String presenterPath) {
        this.presenterPath = presenterPath;
        return this;
    }

    public String getIserviceClassName() {
        return iserviceClassName;
    }

    public HttpConfig setIserviceClassName(String iserviceClassName) {
        this.iserviceClassName = iserviceClassName;
        return this;
    }

    public String getServiceClassName() {
        return serviceClassName;
    }

    public HttpConfig setServiceClassName(String serviceClassName) {
        this.serviceClassName = serviceClassName;
        return this;
    }
}
