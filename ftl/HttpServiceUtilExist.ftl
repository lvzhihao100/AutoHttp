package ${httppackage};
<#if isBean==true>
import ${modelpackage}.${bean};
<#else >
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
    @Override
<#if isBean==true>
    <#if isList==true>
    public <#assign x=0,count=0>
        <#list data.requestParams as param>
            <#assign count=count+1 />
        </#list>
        ${data.methodTypeName}Request<HttpData<List<${bean}>>> ${data.methodName}(<#list data.requestParams as param><#assign x=x+1 /><#if x==count >String ${param.key}<#else>String ${param.key},</#if></#list>){
        return OkGo.<HttpData<List<${bean}>>>${data.methodTypeName?lower_case}(${httpConfigClassName}.BASE_URL + ${httpConfigClassName}.${data.constantName})<#list data.requestParams as param>
                .params("${param.key}", ${param.key})</#list>;
    }
    <#else >
        public <#assign x=0,count=0>
        <#list data.requestParams as param>
            <#assign count=count+1 />
        </#list>
        ${data.methodTypeName}Request<HttpData<${bean}>> ${data.methodName}(<#list data.requestParams as param><#assign x=x+1 /><#if x==count >String ${param.key}<#else>String ${param.key},</#if></#list>){
        return OkGo.<HttpData<${bean}>>${data.methodTypeName?lower_case}(${httpConfigClassName}.BASE_URL + ${httpConfigClassName}.${data.constantName})<#list data.requestParams as param>
                .params("${param.key}", ${param.key})</#list>;
    }
    </#if>
<#else>
    public <#assign x=0,count=0>
        <#list data.requestParams as param>
            <#assign count=count+1 />
        </#list>
        ${data.methodTypeName}Request<HttpData> ${data.methodName}(<#list data.requestParams as param><#assign x=x+1 /><#if x==count >String ${param.key}<#else>String ${param.key},</#if></#list>){
        return OkGo.<HttpData>${data.methodTypeName?lower_case}(${httpConfigClassName}.BASE_URL + ${httpConfigClassName}.${data.constantName})<#list data.requestParams as param>
                .params("${param.key}", ${param.key})</#list>;
    }
</#if>
}