<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/views/commons/taglibs.jspf" %>

<script type="text/javascript">
define('body', ['configs', 'jquery'], function(configs, $){
	$.param.setDefaultParams({
		page: '${param.page}',
		condition: '${param.condition}',
		keyword: '${param.keyword}'
	});
	
    $('form.content').validate({
        rules: {
            title: {
                required: true,
                rangelength: [2, 10]
            },
            content: {
                required: true,
                maxlength: 2000
            }
        },
        messages: {
            title: {
                required: '제목을 입력하세요.',
                rangelength: '제목은 {0}자에서 {1}자까지 입력가능 합니다.'
            },
            content: {
                required: '내용을 입력하세요.',
                maxlength: '내용은 {0}자까지 입력 가능 합니다.'
            }
        }
    });
});
</script>

<c:url var="urlUpdate" value="/board/update"/>
<form:form method="post" commandName="content" action="${urlUpdate}" class="content">
	<form:hidden path="id"/>
	<table>
		<tr>
			<td>제목</td>
			<td>
				<form:input path="title"/>
				<form:errors path="title"/>
			</td>
		</tr>
		<tr>
			<td>내용</td>
			<td>
				<form:input path="content"/>
				<form:errors path="content"/>
			</td>
		</tr>
		<tr>
			<td colspan="2" align="center">
				<input type="submit" value="확인"/>
				<a href="<c:url value="/board/view/${content.id}"/>" class="link-param">취소</a>
			</td>
		</tr>
	</table>
</form:form>
