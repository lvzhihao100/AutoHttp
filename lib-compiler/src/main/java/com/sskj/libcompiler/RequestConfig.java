package com.sskj.libcompiler;

import com.sskj.libcompiler.bean.ClassFiled;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者 :吕志豪
 * 简书：https://www.jianshu.com/u/6e525b929aac
 * github：https://github.com/lvzhihao100
 * 描述：
 * 创建时间：2018-09-25 19:25
 */
public class RequestConfig {
    String methodType = "GET";

    String methodTypeName="Get";
    String path;

    String methodName;

    String constantName;

    String methodDesc;
    String modelName;
    String presenterClassName;
    ClassFiled classFiled;

    public String getPresenterClassName() {
        return presenterClassName;
    }

    public void setPresenterClassName(String presenterClassName) {
        this.presenterClassName = presenterClassName;
    }

    public String getMethodTypeName() {
        return methodTypeName;
    }

    public RequestConfig setMethodTypeName(String methodTypeName) {
        this.methodTypeName = methodTypeName;
        return this;
    }

    public ClassFiled getClassFiled() {
        return classFiled;
    }

    public RequestConfig setClassFiled(ClassFiled classFiled) {
        this.classFiled = classFiled;
        return this;
    }

    boolean isList=false;
    boolean isParseModel = true;

    boolean isCreateModel = true;

    boolean isNoHTTP = false;
    boolean isCreatePresenter=false;

    List<RequestParam> requestParams;

    public String getModelName() {
        return modelName;
    }

    public RequestConfig setModelName(String modelName) {
        this.modelName = modelName;
        return this;
    }

    public RequestConfig() {
        this.requestParams = new ArrayList<>();
    }

    public List<RequestParam> getRequestParams() {
        return requestParams;
    }

    public RequestConfig setRequestParams(List<RequestParam> requestParams) {
        this.requestParams = requestParams;
        return this;
    }

    public String getMethodType() {
        return methodType;
    }

    public RequestConfig setMethodType(String methodType) {
        this.methodType = methodType;
        return this;
    }

    public String getPath() {
        return path;
    }

    public RequestConfig setPath(String path) {
        this.path = path;
        return this;
    }

    public String getMethodName() {
        return methodName;
    }

    public RequestConfig setMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public String getConstantName() {
        return constantName;
    }

    public RequestConfig setConstantName(String constantName) {
        this.constantName = constantName;
        return this;
    }

    public String getMethodDesc() {
        return methodDesc;
    }

    public RequestConfig setMethodDesc(String methodDesc) {
        this.methodDesc = methodDesc;
        return this;
    }

    public boolean isParseModel() {
        return isParseModel;
    }

    public RequestConfig setParseModel(boolean parseModel) {
        isParseModel = parseModel;
        return this;
    }

    public boolean isCreateModel() {
        return isCreateModel;
    }

    public RequestConfig setCreateModel(boolean createModel) {
        isCreateModel = createModel;
        return this;
    }

    public boolean isNoHTTP() {
        return isNoHTTP;
    }

    public RequestConfig setNoHTTP(boolean noHTTP) {
        isNoHTTP = noHTTP;
        return this;
    }
}
