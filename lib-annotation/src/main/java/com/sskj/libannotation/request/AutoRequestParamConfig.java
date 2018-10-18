package com.sskj.libannotation.request;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 作者 :吕志豪
 * 简书：https://www.jianshu.com/u/6e525b929aac
 * github：https://github.com/lvzhihao100
 * 描述：
 * 创建时间：2018-09-13 11:14
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface AutoRequestParamConfig {
    String desc() default "";
    String value() default "";
    String key() default "";
}
