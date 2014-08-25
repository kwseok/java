<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="CONTEXT_PATH" value="${pageContext.request.contextPath}"/>
<html>  
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=8" />
<title>Test application</title>	
<script type="text/javascript" src="${CONTEXT_PATH}/webjars/requirejs/2.1.14/require.js"
        data-main="${CONTEXT_PATH}/resources/js/main"></script>
<script type="text/javascript">
require.config({
    paths: {
        'jquery-extends-locale_ko': '../../webjars/jquery-extends/1.0.0/i18n/jquery.extends-locale_ko'
    },
	shim: {
		'jquery-extends-locale_ko': { deps: ['jquery-extends'] }
	}
});
define('configs',{
	CONTEXT_PATH: '${CONTEXT_PATH}'
});
define('app',[
	'configs',
	'jquery',
	'jquery-cookie',
    'jquery-validation',
    'jquery-validation-additional-methods',
    'jquery-extends',
	'jquery-form',
	'jquery-param',
	'jquery-tmpl',
	'jquery-qtip',
	'jquery-validation-extends',
    'jquery-validation-qtip',
	'jquery-inputmask',
	'jquery-inputmask-extensions',
	'jquery-inputmask-date-extensions',
	'jquery-inputmask-numeric-extensions',
    'jquery-inputmask-phone-extensions',
    'jquery-inputmask-regex-extensions',
	'jquery-bpopup',
	'jquery-popup',
	'jquery-applyfield',
	'jquery-extends-locale_ko'
], function(configs, $){
	$(document).ready(function(){
		$('.link-param').click(function(e){
			e.preventDefault();
			location.href = $.param.makeUrl($(this).attr('href'));
		});

        require(['body'], function(){
            console.log('loaded body script');
        }, function(err){
            // IGNORED
        });
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