<%@ tag display-name="Webjars basedir tag" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="path" type="java.lang.String" description="The webjar path." required="true" %>
<c:out value="${pageContext.request.contextPath}/webjars${path.startsWith('/') ? '':'/'}${path}"/>