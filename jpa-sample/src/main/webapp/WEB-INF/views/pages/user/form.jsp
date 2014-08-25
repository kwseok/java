<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/views/commons/taglibs.jspf" %>

<script type="text/javascript">
define('body', ['configs', 'jquery'], function(configs, $){
    $('form.user').validate({
        rules: {
            username: {
                required: true
            },
            password: {
                required: true
            },
            name: {
                required: true
            }
        },
        messages: {
            username: {
                required: '아이디를 입력하세요.'
            },
            password: {
                required: '패스워드를 입력하세요.'
            },
            name: {
                required: '사용자 이름을 입력하세요.'
            }
        }
    });
});
</script>

<c:url var="urlUserUpdate" value="/user/update"/>
<form:form method="post" commandName="user" action="${urlUserUpdate}" class="user">
	<form:hidden path="id"/>
	<table>
		<tr>
			<td>아이디</td>
			<td>
				<form:input path="username"/>
				<form:errors path="username"/>
			</td>
		</tr>
		<tr>
			<td>패스워드</td>
			<td>
				<form:password path="password"/>
				<form:errors path="password"/>
			</td>
		</tr>
		<tr>
			<td>이름</td>
			<td>
				<form:input path="name"/>
				<form:errors path="name"/>
			</td>
		</tr>
		<tr>
			<td colspan="2" align="center">
				<input type="submit" value="확인"/>
			</td>
		</tr>
	</table>
</form:form>
