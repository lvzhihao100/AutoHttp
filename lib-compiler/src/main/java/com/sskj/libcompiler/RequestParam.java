package com.sskj.libcompiler;

/**
 * 作者 :吕志豪
 * 简书：https://www.jianshu.com/u/6e525b929aac
 * github：https://github.com/lvzhihao100
 * 描述：
 * 创建时间：2018-09-26 08:56
 */
public class RequestParam {
    String value;
    String desc;
    String key;

    public String getKey() {
        return key;
    }

    public RequestParam setKey(String key) {
        this.key = key;
        return this;
    }

    public String getValue() {
        return value;
    }

    public RequestParam setValue(String value) {
        this.value = value;
        return this;
    }

    public String getDesc() {
        return desc;
    }

    public RequestParam setDesc(String desc) {
        this.desc = desc;
        return this;
    }
}
