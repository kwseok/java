<%@ tag display-name="Style tag" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="href" type="java.lang.String" description="The style href" required="false" %>
<link type="text/css" href="<c:url value="${href}"/>" rel="stylesheet"/>