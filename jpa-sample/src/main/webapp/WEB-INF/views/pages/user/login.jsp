<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/views/commons/taglibs.jspf" %>

<script type="text/javascript">
require(['jquery', 'jquery-form', 'jquery-validation', 'jquery-i18n-properties'], function($){
    $('form.login').validate({
        submitHandler: function(form) {
            $.ajax({
                url : $(form).attr('action'),
                type : $(form).attr('method'),
                dataType: 'json',
                data: $(form).serialize(),
                success: function(data){
                    if (!data.result) {
                        if (data.disabled) {
                            alert(data.reason || '서비스 사용이 중지되었습니다.');
                        } else {
                            alert('로그인에 실패하였습니다.')
                        }
                        return;
                    }

                    if (location.pathname == '<c:url value="/user/login"/>')
                        location.replace('<c:url value="/"/>');
                    else
                        location.reload();

                },
                error: function(){ alert('처리 에러.'); }
            });
        },
        rules: {
            loginId: {
                required: true,
                rangelength: [2, 8]
            },
            loginPassword: {
                required: true,
                rangelength: [2, 8]
            }
        },
        messages: {
            loginId: {
                required: '아이디를 입력하세요.',
                rangelength: '아이디는 {0}자에서 {1}자까지 입력가능 합니다.'
            },
            loginPassword: {
                required: '패스워드를 입력하세요.',
                rangelength: '패스워드는 {0}자에서 {1}자까지 입력가능 합니다.'
            }
        }
    });
});
</script>
<form method="post" action="<c:url value="/user/security"/>" class="login">
<table>
	<tr>
		<td>아이디</td>
		<td>
            <input type="text" name="loginId" value=""/>
        </td>
	</tr>
	<tr>
		<td>패스워드</td>
		<td>
			<input type="password" name="loginPassword" value=""/>
		</td>
	</tr>
	<tr>
		<td colspan="2" align="center">
			<input type="submit" value="확인"/>
			<input type="button" value="가입" onclick="location='<c:url value="/user/join"/>';"/>
		</td>
	</tr>
</table>
</form>
