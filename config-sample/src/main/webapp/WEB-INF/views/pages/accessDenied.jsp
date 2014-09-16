<%@ page contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" %>
<%@ include file="/WEB-INF/views/commons/taglibs.jspf" %>
<fmt:message var="defaultErrorMessage" key="error.accessDenied"/>
<c:out value="${errorMessage}" default="${defaultErrorMessage}"/>
