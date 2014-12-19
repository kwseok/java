<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/views/commons/taglibs.jspf" %>

<fmt:message var="defaultErrorMessage" key="error.badRequest"/>
<h2><c:out value="${errorMessage}" default="${defaultErrorMessage}"/></h2>
<div class="HT25_empty_space"></div>
<c:if test="${not empty fieldErrorMessages}">
	<h4>Field errors</h4>

	<c:forEach var="fieldError" items="${fieldErrorMessages}">
		<c:out value="${fieldError.key}" /> : <c:out value="${fieldError.value}" /> <br/>
	</c:forEach>
</c:if>