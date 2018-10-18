package ${httppackage};
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;
import java.util.HashMap;
import java.util.List;
<#if isBean=true>

import ${modelpackage}.${bean};
<#else >
</#if>
/**
 * 作者 :吕志豪
 * 简书：https://www.jianshu.com/u/6e525b929aac
 * github：https://github.com/lvzhihao100
 * 描述：网络接口
 */
public interface ${IServiceClassName} {


    /**
     * ${data.methodDesc}
     *
<#list data.requestParams as param>
     * @param ${param.key} ${param.desc}
</#list>
     * @return
     */
<#if isBean=true>
    <#if isList==true>
        <#assign x=0,count=0>
        <#list data.requestParams as param>
            <#assign count=count+1 />
        </#list>
        ${data.methodTypeName}Request<HttpData<List<${bean}>>> ${data.methodName}(<#list data.requestParams as param><#assign x=x+1 /><#if x==count >String ${param.key}<#else>String ${param.key},</#if></#list>);
    <#else>
        <#assign x=0,count=0>
        <#list data.requestParams as param>
            <#assign count=count+1 />
        </#list>
        ${data.methodTypeName}Request<HttpData<${bean}>> ${data.methodName}(<#list data.requestParams as param><#assign x=x+1 /><#if x==count >String ${param.key}<#else>String ${param.key},</#if></#list>);
    </#if>
<#else >
        <#assign x=0,count=0>
        <#list data.requestParams as param>
            <#assign count=count+1 />
        </#list>
        ${data.methodTypeName}Request<HttpData<${bean}>> ${data.methodName}(<#list data.requestParams as param><#assign x=x+1 /><#if x==count >String ${param.key}<#else>String ${param.key},</#if></#list>);
</#if>
}