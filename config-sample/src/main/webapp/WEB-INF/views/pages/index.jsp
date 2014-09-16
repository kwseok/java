<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="site" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="webjars" tagdir="/WEB-INF/tags/webjars" %>
<!doctype html>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ko" lang="ko">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=8"/>
    <%@ include file="/WEB-INF/views/commons/head.jspf" %>
    <site:script src="/webjars/setupRequireJS.js"/>
    <webjars:requirejs module="/assets/js/site/main"/>
</head>
<body class="ng-cloak">
<ui-view></ui-view>
</body>
</html>