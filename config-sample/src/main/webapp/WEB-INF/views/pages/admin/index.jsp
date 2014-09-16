<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="site" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="webjars" tagdir="/WEB-INF/tags/webjars" %>
<!doctype html>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ko" lang="ko">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=8"/>
    <%@ include file="/WEB-INF/views/commons/head.jspf" %>
    <site:style href="/assets/css/admin.css"/>
    <webjars:style path="css/bootstrap.min.css"/>
    <webjars:style path="loading-bar.min.css"/>
    <site:script src="/webjars/setupRequireJS.js"/>
    <webjars:requirejs module="/assets/js/admin/main"/>
</head>
<body class="ng-cloak">

<!-- Top Navigation -->
<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" ui-sref="main">GLAS</a>
        </div>

        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
                <!--
                <li class="active"><a href="#">활성화</a></li>
                -->
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">업데이트관리 <span class="caret"></span></a>
                    <ul class="dropdown-menu" role="menu">
                        <li><a href="#">업데이트 기준일자</a></li>
                        <li><a href="#">보고서관리</a></li>
                    </ul>
                </li>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">회원정보관리 <span class="caret"></span></a>
                    <ul class="dropdown-menu" role="menu">
                        <li><a href="#">회원관리</a></li>
                        <li><a href="#">방문요청관리</a></li>
                        <li><a href="#">관심특허요청</a></li>
                        <li><a href="#">관심특허등록</a></li>
                        <li class="divider"></li>
                        <li><a href="#">로그인내역</a></li>
                        <li><a href="#">메일링</a></li>
                    </ul>
                </li>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">게시판관리 <span class="caret"></span></a>
                    <ul class="dropdown-menu" role="menu">
                        <li><a href="#">공지사항</a></li>
                        <li><a href="#">관련법규</a></li>
                    </ul>
                </li>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">정보관리 <span class="caret"></span></a>
                    <ul class="dropdown-menu" role="menu">
                        <li><a href="#">그린리스트</a></li>
                        <li><a ui-sref="info-patent">특허관리</a></li>
                        <li><a href="#">소송정보(1심)</a></li>
                        <li><a href="#">소송정보(2/3심)</a></li>
                    </ul>
                </li>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">수집정보관리 <span class="caret"></span></a>
                    <ul class="dropdown-menu" role="menu">
                        <li><a ui-sref="kipris-trialChanges">소송변경사항</a></li>
                        <li><a href="#">수집상태</a></li>
                        <li><a href="#">특허정보</a></li>
                        <li><a href="#">소송정보</a></li>
                    </ul>
                </li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li><a href="#">로그아웃</a></li>
                <li><a href="/" target="_blank">홈페이지</a></li>
            </ul>
        </div><!-- /.navbar-collapse -->
    </div><!-- /.container-fluid -->
</nav>

<ui-view>
</ui-view>

</body>
</html>