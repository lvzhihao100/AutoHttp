package com.sskj.libcompiler.bean;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.*;

import java.io.*;
import java.util.HashMap;
import java.util.List;

public class Option implements TemplateMethodModelEx {
    @Override
    public Object exec(List arguments)  {
        try {
            Configuration configuration = new Configuration();
            configuration.setDirectoryForTemplateLoading(new File(""));
            configuration.setObjectWrapper(new DefaultObjectWrapper());
            configuration.setDefaultEncoding("UTF-8");   //这个一定要设置，不然在生成的页面中 会乱码
            //获取或创建一个模版。
            ClassTemplateLoader ctl = new ClassTemplateLoader(getClass(), "/");
            configuration.setTemplateLoader(ctl);
            //获取或创建一个模版。
            Template template = configuration.getTemplate("modelfiled.ftl");
            HashMap<String, Object> map = new HashMap<>();
            map.put("data", arguments.get(0));
            map.put("option", new Option());
            Writer writer = new StringWriter();
            template.process(map, writer);
            return writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
        return "";
    }
}
