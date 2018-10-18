package ${httppackage};
<#if isBean>
import ${modelpackage}.${bean};
</#if>
${content}

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
    <#else >
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