<#--noinspection FtlReferencesInspection-->
<#assign authentication = (Session.SPRING_SECURITY_CONTEXT.authentication)!>

<#macro hasAnyAuthority authorities>
    <#list authorities as authority>
        <#if hasRole(authority)>
            <#nested>
            <#break>
        </#if>
    </#list>
</#macro>

<#function isAuthentication>
    <#return (authentication?keys)?size gt 0>
</#function>

<#function isAdmin>
    <#return authentication.principal.isAdmin()>
</#function>

<#function getCurrentUser>
    <#return (authentication.principal)!{}>
</#function>

<#function hasAuthority authority>
    <#local rtnVal = false>
    <#if isAuthentication()>
        <#list (authentication.authorities)![] as item>
            <#if authority == item>
                <#local rtnVal = true>
                <#break>
            </#if>
        </#list>
    </#if>
    <#return rtnVal>
</#function>