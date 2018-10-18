package com.sskj.libcompiler;

import com.arronlong.httpclientutil.HttpClientUtil;
import com.google.auto.service.AutoService;
import com.sskj.libannotation.base.AutoHttpConfig;
import com.sskj.libannotation.request.AutoRequestConfig;
import com.sskj.libannotation.request.AutoRequestParamConfig;
import com.sskj.libcompiler.bean.ClassFiled;
import com.sskj.libcompiler.bean.Option;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * @author hiphonezhu@gmail.com
 * @version [CompilerAnnotation, 17/6/20 09:55]
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.sskj.libannotation.base.AutoHttpConfig"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class AutoHttpInjectProcessor extends AbstractProcessor {
    private static final String PAKAGE = "src/main/java/";
    Elements elementUtils;
    private HttpConfig httpConfig;
    private Map<String, RequestConfig> requestConfigMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(AutoHttpConfig.class);
        if (!elements.iterator().hasNext()) {
            System.out.println("AutoHttpConfig 主配置未找到");
            return true;
        }

        searchHttpConfig(roundEnvironment);
        Set<? extends Element> elementsRequests = roundEnvironment.getElementsAnnotatedWith(AutoRequestConfig.class);
        if (!elementsRequests.iterator().hasNext()) {
            System.out.println("AutoRequestConfig 配置未找到，至少一个");
            return true;
        }
        searchRequestsConfig(roundEnvironment);
        searchRequestsParam(roundEnvironment);
        request();
        try {
            Configuration configuration = new Configuration();
            configuration.setDirectoryForTemplateLoading(new File(""));
            configuration.setObjectWrapper(new DefaultObjectWrapper());
            configuration.setDefaultEncoding("UTF-8");   //这个一定要设置，不然在生成的页面中 会乱码
            configuration.setTemplateLoader(new FileTemplateLoader(new File(httpConfig.ftlPath)));
            generateModel(configuration);
            generateIHttpService(configuration);
            generateHttpService(configuration);
            generateHttpConfig(configuration);
            generatePresenter(configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void generatePresenter(Configuration configuration) {
        requestConfigMap.forEach((key, requestConfig) -> {
            if (requestConfig.isCreatePresenter) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("data", requestConfig);
                map.put("httppackage", httpConfig.getServicePath());
                map.put("modelpackage", httpConfig.getBeanPath());
                map.put("IServiceClassName", httpConfig.getIserviceClassName());
                map.put("ServiceClassName", httpConfig.getServiceClassName());
                map.put("httpConfigClassName", httpConfig.httpConfigClassName);
                map.put("bean", requestConfig.modelName);
                map.put("isList", requestConfig.isList);
                map.put("isBean", requestConfig.isCreateModel);
                File file = new File(httpConfig.moduleName + PAKAGE + httpConfig.getPresenterPath().replace(".", "/"));
                if (!file.exists()) {
                    file.mkdirs();
                }
                File fileIservice = new File(httpConfig.moduleName + PAKAGE + httpConfig.getPresenterPath().replace(".", "/"), requestConfig.getPresenterClassName() + ".java");
                try {
                    if (!fileIservice.exists()) {
                        System.out.println(fileIservice.getName() + "不存在");
                    } else {
                        String s = fileRead(fileIservice);
                        s = s.replaceAll("}([\\s])*$", "");
                        map.put("content", s);

                        Template template = configuration.getTemplate("HttpPresenter.ftl");
                        Writer writer = new OutputStreamWriter(new FileOutputStream(fileIservice), "UTF-8");
                        template.process(map, writer);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TemplateException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void generateHttpConfig(Configuration configuration) {
        requestConfigMap.forEach((key, requestConfig) -> {
            HashMap<String, Object> map = new HashMap<>();
            map.put("data", requestConfig);
            map.put("httppackage", httpConfig.getServicePath());
            map.put("modelpackage", httpConfig.getBeanPath());
            map.put("IServiceClassName", httpConfig.getIserviceClassName());
            map.put("ServiceClassName", httpConfig.getServiceClassName());
            map.put("httpConfigClassName", httpConfig.httpConfigClassName);
            map.put("bean", requestConfig.modelName);
            map.put("isList", requestConfig.isList);
            map.put("isBean", requestConfig.isCreateModel);
            File file = new File(httpConfig.moduleName + PAKAGE + httpConfig.getServicePath().replace(".", "/"));
            if (!file.exists()) {
                file.mkdirs();
            }
            File fileIservice = new File(httpConfig.moduleName + PAKAGE + httpConfig.getServicePath().replace(".", "/"), httpConfig.getHttpConfigClassName() + ".java");
            try {
                if (!fileIservice.exists()) {
                    Template template = configuration.getTemplate("HttpConfig.ftl");
                    Writer writer = new OutputStreamWriter(new FileOutputStream(fileIservice), "UTF-8");
                    template.process(map, writer);
                } else {
                    String s = fileReadNoFirstLIne(fileIservice);
                    s = s.replaceAll("}([\\s])*$", "");
                    map.put("content", s);

                    Template template = configuration.getTemplate("HttpConfigExist.ftl");
                    Writer writer = new OutputStreamWriter(new FileOutputStream(fileIservice), "UTF-8");
                    template.process(map, writer);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TemplateException e) {
                e.printStackTrace();
            }
        });
    }

    private void generateHttpService(Configuration configuration) {
        requestConfigMap.forEach((key, requestConfig) -> {
            HashMap<String, Object> map = new HashMap<>();
            map.put("data", requestConfig);
            map.put("httppackage", httpConfig.getServicePath());
            map.put("modelpackage", httpConfig.getBeanPath());
            map.put("IServiceClassName", httpConfig.getIserviceClassName());
            map.put("ServiceClassName", httpConfig.getServiceClassName());
            map.put("httpConfigClassName", httpConfig.httpConfigClassName);
            map.put("bean", requestConfig.modelName);
            map.put("isList", requestConfig.isList);
            map.put("isBean", requestConfig.isCreateModel);
            File file = new File(httpConfig.moduleName + PAKAGE + httpConfig.getServicePath().replace(".", "/"));
            if (!file.exists()) {
                file.mkdirs();
            }
            File fileIservice = new File(httpConfig.moduleName + PAKAGE + httpConfig.getServicePath().replace(".", "/"), httpConfig.getServiceClassName() + ".java");
            try {
                if (!fileIservice.exists()) {
                    Template template = configuration.getTemplate("HttpServiceUtil.ftl");
                    Writer writer = new OutputStreamWriter(new FileOutputStream(fileIservice), "UTF-8");
                    template.process(map, writer);
                } else {
                    String s = fileReadNoFirstLIne(fileIservice);
                    s = s.replaceAll("}([\\s])*$", "");
                    map.put("content", s);

                    Template template = configuration.getTemplate("HttpServiceUtilExist.ftl");
                    Writer writer = new OutputStreamWriter(new FileOutputStream(fileIservice), "UTF-8");
                    template.process(map, writer);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TemplateException e) {
                e.printStackTrace();
            }
        });
    }

    private void generateIHttpService(Configuration configuration) {
        requestConfigMap.forEach((key, requestConfig) -> {
            HashMap<String, Object> map = new HashMap<>();
            map.put("data", requestConfig);
            map.put("httppackage", httpConfig.getServicePath());
            map.put("modelpackage", httpConfig.getBeanPath());
            map.put("IServiceClassName", httpConfig.getIserviceClassName());
            map.put("httpConfigClassName", httpConfig.httpConfigClassName);
            map.put("bean", requestConfig.modelName);
            map.put("isList", requestConfig.isList);
            map.put("isBean", requestConfig.isCreateModel);
            File file = new File(httpConfig.moduleName + PAKAGE + httpConfig.getServicePath().replace(".", "/"));
            if (!file.exists()) {
                file.mkdirs();
            }
            File fileIservice = new File(httpConfig.moduleName + PAKAGE + httpConfig.getServicePath().replace(".", "/"), httpConfig.getIserviceClassName() + ".java");
            try {
                if (!fileIservice.exists()) {
                    Template template = configuration.getTemplate("IHttpService.ftl");
                    Writer writer = new OutputStreamWriter(new FileOutputStream(fileIservice), "UTF-8");
                    template.process(map, writer);
                } else {
                    String s = fileReadNoFirstLIne(fileIservice);
                    s = s.replaceAll("}([\\s])*$", "");
                    map.put("content", s);

                    Template template = configuration.getTemplate("IHttpServiceExist.ftl");
                    Writer writer = new OutputStreamWriter(new FileOutputStream(fileIservice), "UTF-8");
                    template.process(map, writer);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TemplateException e) {
                e.printStackTrace();
            }
        });

    }

    private String fileReadNoFirstLIne(File file) {
        try {
            FileReader reader = new FileReader(file);//定义一个fileReader对象，用来初始化BufferedReader
            BufferedReader bReader = new BufferedReader(reader);//new一个BufferedReader对象，将文件内容读取到缓存
            StringBuilder sb = new StringBuilder();//定义一个字符串缓存，将字符串存放缓存中
            String s = "";
            bReader.readLine();
            while ((s = bReader.readLine()) != null) {//逐行读取文件内容，不读取换行符和末尾的空格
                sb.append(s + "\n");//将读取的字符串添加换行符后累加存放在缓存中
                System.out.println(s);
            }
            bReader.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String fileRead(File file) {
        try {
            FileReader reader = new FileReader(file);//定义一个fileReader对象，用来初始化BufferedReader
            BufferedReader bReader = new BufferedReader(reader);//new一个BufferedReader对象，将文件内容读取到缓存
            StringBuilder sb = new StringBuilder();//定义一个字符串缓存，将字符串存放缓存中
            String s = "";
            while ((s = bReader.readLine()) != null) {//逐行读取文件内容，不读取换行符和末尾的空格
                sb.append(s + "\n");//将读取的字符串添加换行符后累加存放在缓存中
                System.out.println(s);
            }
            bReader.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void generateModel(Configuration configuration) {
        requestConfigMap.forEach((s, requestConfig) -> {
            if (requestConfig.isCreateModel) {
                try {
                    madeBean(configuration, requestConfig.getClassFiled(), requestConfig.isParseModel);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TemplateException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void request() {
        requestConfigMap.forEach((s, requestConfig) -> {
            if (!requestConfig.isNoHTTP) {
                ClassFiled first = new ClassFiled(requestConfig.getModelName());
                try {
                    Map<String, Object> paramMap = new HashMap<>();
                    requestConfig.getRequestParams().forEach(requestParam -> {
                        paramMap.put(requestParam.getKey(), requestParam.getValue());
                    });
                    Header header = new BasicHeader("token", "123");
                    Header[] headers = new Header[1];
                    headers[0] = header;
                    String html;
                    if (requestConfig.getMethodType().equals("POST")) {
                        com.arronlong.httpclientutil.common.HttpConfig custom = com.arronlong.httpclientutil.common.HttpConfig
                                .custom()
                                .headers(headers)
                                .map(paramMap)
                                .url(httpConfig.getBaseUrl() + requestConfig.getPath());
                        html = HttpClientUtil.post(custom).replace("\\", "");
                    } else {
                        Iterator<String> iterator = paramMap.keySet().iterator();
                        StringBuilder stringBuilder = new StringBuilder("?");
                        while (iterator.hasNext()) {
                            String next = iterator.next();
                            String value = (String) paramMap.get(next);
                            stringBuilder.append(next + "=" + value + "&");
                        }
                        String param = stringBuilder.substring(0, stringBuilder.length() - 1);
                        com.arronlong.httpclientutil.common.HttpConfig custom = com.arronlong.httpclientutil.common.HttpConfig
                                .custom()
                                .headers(headers)
                                .map(paramMap)
                                .url(httpConfig.getBaseUrl() + requestConfig.getPath() + param);
                        html = HttpClientUtil.get(custom).replace("\\", "");
                    }
                    System.out.println(html);
                    try {
                        JSONObject jsonObject = new JSONObject(html);
                        Object data = jsonObject.get("data");
                        if (data != null) {
                            if (data instanceof JSONArray) {
                                requestConfig.isList = true;
                                JSONArray jsonArray = (JSONArray) data;
                                analyzeModel(jsonArray.get(0), first);
                            } else {
                                requestConfig.isList = false;
                                analyzeModel(data, first);
                            }
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                        requestConfig.isParseModel = false;
                        requestConfig.setClassFiled(first);

                    }
                    requestConfig.setClassFiled(first);
                } catch (Exception e) {
                    requestConfig.isParseModel = false;
                    requestConfig.setClassFiled(first);
                    System.out.println(e);
                }
            } else {
                if (requestConfig.isCreateModel) {
                    requestConfig.classFiled = new ClassFiled(requestConfig.getModelName());
                }
            }
        });
    }

    private void searchRequestsParam(RoundEnvironment roundEnvironment) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(AutoRequestParamConfig.class);
        for (Element element : elements) {
            List<? extends AnnotationMirror> annotationMirrors = element.getAnnotationMirrors();
            AnnotationMirror annotationMirror = annotationMirrors.get(0);
            Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues = annotationMirror.getElementValues();
            Iterator<? extends Map.Entry<? extends ExecutableElement, ? extends AnnotationValue>> iterator = elementValues.entrySet().iterator();
            RequestParam requestParam = new RequestParam();
            requestParam.setKey(element.getSimpleName().toString());
            // 备注解元素所在的Class
            TypeElement typeElement = (TypeElement) element.getEnclosingElement();
            // Class的完整路径
            String classFullName = typeElement.getQualifiedName().toString();
            while (iterator.hasNext()) {
                Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> next = iterator.next();
                ExecutableElement key = next.getKey();
                String s = key.toString();
                switch (s) {
                    case "desc()":
                        requestParam.desc = next.getValue().toString().replace("\"", "");
                        requestParam.desc = unicodeToCn(requestParam.desc);

                        break;
                    case "value()":
                        requestParam.value = next.getValue().toString().replace("\"", "");
                        break;

                }
            }
            requestConfigMap.get(classFullName).getRequestParams().add(requestParam);
        }
    }

    private void searchRequestsConfig(RoundEnvironment roundEnvironment) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(AutoRequestConfig.class);
        if (!elements.iterator().hasNext()) {
            System.out.println("AutoRequestConfig 配置未找到，至少一个");
        }
        for (Element element : elements) {
            List<? extends AnnotationMirror> annotationMirrors = element.getAnnotationMirrors();
            AnnotationMirror annotationMirror = annotationMirrors.get(0);
            Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues = annotationMirror.getElementValues();
            Iterator<? extends Map.Entry<? extends ExecutableElement, ? extends AnnotationValue>> iterator = elementValues.entrySet().iterator();
            RequestConfig requestConfig = new RequestConfig();
            // Class的完整路径
            String classFullName = ((TypeElement) element).getQualifiedName().toString();
            while (iterator.hasNext()) {
                Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> next = iterator.next();
                ExecutableElement key = next.getKey();
                String s = key.toString();
                switch (s) {
                    case "methodType()":
                        requestConfig.methodType = next.getValue().toString().replace("\"", "");
                        requestConfig.methodTypeName = requestConfig.methodType.equals("GET") ? "Get" : "Post";
                        break;
                    case "path()":
                        requestConfig.path = next.getValue().toString().replace("\"", "");
                        break;
                    case "methodName()":
                        requestConfig.methodName = next.getValue().toString().replace("\"", "");
                        break;
                    case "constantName()":
                        requestConfig.constantName = next.getValue().toString().replace("\"", "");
                        break;
                    case "methodDesc()":
                        requestConfig.methodDesc = next.getValue().toString().replace("\"", "");
                        requestConfig.methodDesc = unicodeToCn(requestConfig.methodDesc);
                        break;
                    case "modelName()":
                        requestConfig.modelName = next.getValue().toString().replace("\"", "");
                        break;
                    case "isParseModel()":
                        requestConfig.isParseModel = next.getValue().toString().equals("true");
                        break;
                    case "isCreateModel()":
                        requestConfig.isCreateModel = next.getValue().toString().equals("true");
                        break;
                    case "isCreatePresenter()":
                        requestConfig.isCreatePresenter = next.getValue().toString().equals("true");
                        break;
                    case "isNoHTTP()":
                        requestConfig.isNoHTTP = next.getValue().toString().equals("true");
                        break;
                    case "presenterClassName()":
                        requestConfig.presenterClassName = next.getValue().toString().replace("\"", "");
                        break;
                }
            }
            requestConfigMap.put(classFullName, requestConfig);
        }
    }

    /**
     * 变汉字
     *
     * @param str
     * @return
     */
    private static String unicodeToCn(String str) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            //group 6728
            String group = matcher.group(2);
            //ch:'木' 26408
            ch = (char) Integer.parseInt(group, 16);
            //group1 \u6728
            String group1 = matcher.group(1);
            str = str.replace(group1, ch + "");
        }
        return str;
    }

    private void searchHttpConfig(RoundEnvironment roundEnvironment) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(AutoHttpConfig.class);
        if (!elements.iterator().hasNext()) {
            System.out.println("AutoHttpConfig 主配置未找到");
            return;
        }
        Element element = elements.iterator().next();
        List<? extends AnnotationMirror> annotationMirrors = element.getAnnotationMirrors();
        AnnotationMirror annotationMirror = annotationMirrors.get(0);
        Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues = annotationMirror.getElementValues();
        Iterator<? extends Map.Entry<? extends ExecutableElement, ? extends AnnotationValue>> iterator = elementValues.entrySet().iterator();
        httpConfig = new HttpConfig();
        while (iterator.hasNext()) {
            Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> next = iterator.next();
            ExecutableElement key = next.getKey();
            String s = key.toString();
            switch (s) {
                case "baseUrl()":
                    httpConfig.baseUrl = next.getValue().toString().replace("\"", "");
                    break;
                case "moduleName()":
                    httpConfig.moduleName = next.getValue().toString().replace("\"", "");
                    break;
                case "beanPath()":
                    httpConfig.beanPath = next.getValue().toString().replace("\"", "");
                    break;
                case "servicePath()":
                    httpConfig.servicePath = next.getValue().toString().replace("\"", "");
                    break;
                case "presenterPath()":
                    httpConfig.presenterPath = next.getValue().toString().replace("\"", "");
                    break;
                case "iserviceClassName()":
                    httpConfig.iserviceClassName = next.getValue().toString().replace("\"", "");
                    break;
                case "serviceClassName()":
                    httpConfig.serviceClassName = next.getValue().toString().replace("\"", "");
                    break;
                case "httpConfigClassName()":
                    httpConfig.httpConfigClassName = next.getValue().toString().replace("\"", "");
                    break;
                case "ftlPath()":
                    httpConfig.ftlPath = next.getValue().toString().replace("\"", "");
                    break;
            }
        }
    }

    private void madeBean(Configuration configuration, ClassFiled classFiled, boolean isParse) throws IOException, TemplateException {
        Template template = configuration.getTemplate("model.ftl");
        HashMap<String, Object> map = new HashMap<>();
        map.put("data", classFiled);
        map.put("option", new Option());
        map.put("isParse", isParse);
        map.put("package", httpConfig.getBeanPath());
        File file = new File(httpConfig.moduleName + PAKAGE + httpConfig.getBeanPath().replace(".", "/"));
        if (!file.exists()) {
            file.mkdirs();
        }
        File file1 = new File(httpConfig.moduleName + PAKAGE + httpConfig.getBeanPath().replace(".", "/"), classFiled.getClassName() + ".java");
        if (file1.exists()) {
            return;
        }
        Writer writer = new OutputStreamWriter(new FileOutputStream(file1), "UTF-8");
        template.process(map, writer);
    }

    private void analyzeModel(Object data, ClassFiled classFiled) {
        if (data instanceof JSONArray) {
            analyzeModel(((JSONArray) data).get(0), classFiled);
        } else {
            JSONObject jsonObject = (JSONObject) data;
            Iterator<String> keys = jsonObject.keys();
            Map<String, List<Object>> filedMap = classFiled.getFiledMap();
            while (keys.hasNext()) {
                List<Object> list = new ArrayList<>();
                String key = keys.next();
                Object o = jsonObject.get(key);
                String value;
                String tip = "";
                if (jsonObject.isNull(key)) {
                    value = "String";
                    list.add(value);
                    list.add(tip);
                } else if (o instanceof Integer) {
                    value = "int";
                    list.add(value);
                    list.add(tip);
                } else if (o instanceof Double) {
                    value = "double";
                    list.add(value);
                    list.add(tip);
                } else if (o instanceof Boolean) {
                    value = "boolean";
                    list.add(value);
                    list.add(tip);
                } else if (o instanceof Long) {
                    value = "long";
                    list.add(value);
                    list.add(tip);
                } else if (o instanceof String) {
                    value = "String";
                    list.add(value);
                    list.add(tip);
                } else if (o instanceof JSONArray) {
                    value = "List<" + key.substring(0, 1).toUpperCase() + key.substring(1, key.length()) + ">";
                    list.add(value);
                    list.add(tip);
                    ClassFiled classFiled1 = new ClassFiled(key.substring(0, 1).toUpperCase() + key.substring(1, key.length()));
                    analyzeModel(((JSONArray) o).get(0), classFiled1);
                    list.add(classFiled1);
                } else {
                    value = key.substring(0, 1).toUpperCase() + key.substring(1, key.length());
                    list.add(value);
                    list.add(tip);
                    ClassFiled classFiled1 = new ClassFiled(key.substring(0, 1).toUpperCase() + key.substring(1, key.length()));
                    analyzeModel(o, classFiled1);
                    list.add(classFiled1);
                }
                filedMap.put(key, list);
            }
        }
    }
}
