<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="CONTEXT_PATH" value="${pageContext.request.contextPath}"/>
<html>  
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=8" />
<title>Test application</title>
    <script type="text/javascript">
        var configs = {
            contextPath: '${CONTEXT_PATH}'
        };
    </script>
    <script type="text/javascript" src="${CONTEXT_PATH}/resources/js/require-config.js"></script>
    <script type="text/javascript" src="${CONTEXT_PATH}/webjars/requirejs/2.1.14/require.js"></script>
    <script type="text/javascript">
        require(['jquery','jquery-extends'], function($){
            $('.link-param').click(function(e){
                e.preventDefault();
                location.href = $.param.makeUrl($(this).attr('href'));
            });
        });
    </script>
<tiles:insertAttribute name="resources" ignore="true"/>
</head>
<body>
<div id="wrapper">
	<div id="header"><tiles:insertAttribute name="header" ignore="true" /></div>
	<div id="menu"><tiles:insertAttribute name="menu" ignore="true" /></div>
	<div id="main"><tiles:insertAttribute name="body"/></div>
	<div id="footer"><tiles:insertAttribute name="footer" ignore="true"/></div>
</div>
</body>
</html>