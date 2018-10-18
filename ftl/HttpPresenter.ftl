${content}
    /**
     * ${data.methodDesc}
     *
<#list data.requestParams as param>
     * @param ${param.key} ${param.desc}
</#list>
     * @return
     */
<#if isBean==true>
    <#if isList==true>
    <#assign x=0,count=0>
        <#list data.requestParams as param>
            <#assign count=count+1 />
        </#list>
        public void ${data.methodName}(
        <#list data.requestParams as param>
            <#assign x=x+1 />
            <#if x==count >
                String ${param.key}
            <#else>
                String ${param.key},
            </#if>
        </#list>){
        ${ServiceClassName?uncap_first}.${data.methodName}(
        <#assign x=0>
        <#list data.requestParams as param>
            <#assign x=x+1 />
            <#if x==count >
                ${param.key}
            <#else>
                ${param.key},
            </#if>
        </#list>)
        .execute(new JsonCallBack<HttpData<List<${bean}>>>() {
                @Override
                 public void onSuccess(Response<HttpData<List<${bean}>>> response) {
                    HttpData<List<${bean}>> httpData = response.body();
                    if (httpData.getStatus() == HttpConfig.OK) {

                    }
        }
    }
    <#else >
           <#assign x=0,count=0>
               <#list data.requestParams as param>
                   <#assign count=count+1 />
               </#list>
      public void ${data.methodName}(<#list data.requestParams as param><#assign x=x+1 /><#if x==count >String ${param.key}<#else>String ${param.key},</#if></#list>){
               ${ServiceClassName?uncap_first}.${data.methodName}(<#assign x=0><#list data.requestParams as param> <#assign x=x+1 /><#if x==count >${param.key}<#else>${param.key},</#if></#list>)
               .execute(new JsonCallBack<HttpData<${bean}>>() {
                       @Override
                        public void onSuccess(Response<HttpData<${bean}>> response) {
                           HttpData<${bean}> httpData = response.body();
                           if (httpData.getStatus() == HttpConfig.OK) {

                           }
               }
           }
    </#if>
<#else>
    <#assign x=0,count=0>
    <#list data.requestParams as param>
        <#assign count=count+1 />
    </#list>
    public void ${data.methodName}(<#list data.requestParams as param><#assign x=x+1 /><#if x==count >String ${param.key}<#else>String ${param.key},</#if></#list>){
        ${ServiceClassName?uncap_first}.${data.methodName}(<#assign x=0><#list data.requestParams as param><#assign x=x+1 /><#if x==count >${param.key}<#else>${param.key},</#if></#list>)
               .execute(new JsonCallBack<HttpData>() {
                       @Override
                        public void onSuccess(Response<HttpData> response) {
                           HttpData httpData = response.body();
                           if (httpData.getStatus() == HttpConfig.OK) {

                           }
               }
           }
        }
</#if>
}
