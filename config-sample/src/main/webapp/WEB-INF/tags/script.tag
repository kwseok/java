<%@ tag display-name="Script tag" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="src" type="java.lang.String" description="The script source" required="false" %>
<script type="text/javascript" src="<c:url value="${src}"/>"></script>