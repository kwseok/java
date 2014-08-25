<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/views/commons/taglibs.jspf" %>

<fmt:message var="defaultErrorMessage" key="errors.accessDenied"/>
<h2><c:out value="${errorMessage}" default="${defaultErrorMessage}"/></h2>
<div class="HT25_empty_space"></div>
<c:if test="${not empty exception}">
	<h4>Details</h4>

	Exception Message
	<c:out value="${exception.localizedMessage}" />

	Exception Stack Trace
	<c:forEach items="${exception.stackTrace}" var="trace">
		<c:out value="${trace}" /> <br/>
	</c:forEach>
</c:if>