package ${package};
public class ${data.className} {
<#if isParse>
<#list data.filedMap?keys as key>
    private ${data.filedMap[key][0]} ${key};
</#list>

<#list data.filedMap?keys as key>
    public void set${key?cap_first} (${data.filedMap[key][0]} ${key}){
                this.${key}=${key};
    }
    public ${data.filedMap[key][0]} get${key?cap_first} (){
               return ${key};
    }
</#list>
<#list data.filedMap?keys as key>

    <#if data.filedMap[key]?size = 3 >
            ${option(data.filedMap[key][2])}
    </#if>
</#list>
</#if>
 }
