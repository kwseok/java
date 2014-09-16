<%@ tag display-name="RequireJS tag" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wj" uri="http://www.webjars.org/tags" %>
<%@ taglib prefix="webjars" tagdir="/WEB-INF/tags/webjars" %>
<%@ attribute name="module" type="java.lang.String" description="The main module" required="false" %>
<wj:locate path="require.js" var="src">
    <c:if test="${not empty module}">
        <c:set var="dataMain">data-main="<c:url value="${module}"/>"</c:set>
    </c:if>
    <script type="text/javascript" src="<webjars:url path="${src}"/>" ${dataMain}></script>
</wj:locate>