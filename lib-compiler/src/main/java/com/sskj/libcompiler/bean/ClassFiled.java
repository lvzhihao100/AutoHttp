package com.sskj.libcompiler.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用于编写java文件的类属性
 */
public class ClassFiled {
    String className;//类名
    Map<String,List<Object>> filedMap;//key 属性名字，value [0] 类型 [1] 注释 [2]对象（ClassFiled）

    public ClassFiled(String className) {
        this.className = className;
        filedMap=new HashMap<>();
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Map<String, List<Object>> getFiledMap() {
        return filedMap;
    }

    public void setFiledMap(Map<String, List<Object>> filedMap) {
        this.filedMap = filedMap;
    }

    @Override
    public String toString() {
        return "ClassFiled{" +
                "className='" + className + '\'' +
                ", filedMap=" + filedMap +
                '}';
    }
}
