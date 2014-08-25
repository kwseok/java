<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/views/commons/taglibs.jspf" %>

<script type="text/javascript">
define('body', ['configs', 'jquery'], function(configs, $){
	$.param.setDefaultParams({
		page: '${param.page}',
		condition: '${param.condition}',
		keyword: '${param.keyword}'
	});

    // Comment submit
    $('form.comment').validate({
        rules: {
            content: {
                required: true,
                maxlength: 2000
            }
        },
        messages: {
            content: {
                required: '내용을 입력하세요.',
                maxlength: '내용은 {0}자까지 입력 가능 합니다.'
            }
        }
    });
});
</script>

<table>
	<tr>
		<td>작성일</td>
		<td>
			<fmt:formatDate value="${content.regDate}" pattern="yyyy-MM-dd HH:mm:ss" />
		</td>
	</tr>
	<tr>
		<td>제목</td>
		<td>
			<c:out value="${content.title}"/>
		</td>
	</tr>
	<tr>
		<td>내용</td>
		<td>
			<c:out value="${content.content}"/>
		</td>
	</tr>
</table>

<!-- 댓글 리스트 -->
<table>
	<c:if test="${empty content.comments}">
	<tr>
		<td>댓글이 없습니다.</td>
	</tr>
	</c:if>
	<c:forEach var="comment" items="${content.comments}">
	<tr>
		<td>
			<c:out value="${comment.content}"/>
		</td>
		<td>
			<fmt:formatDate value="${comment.regDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
		</td>
		<td>
			<a href="<c:url value="/board/${content.id}/comment/delete/${comment.id}"/>" class="link-param">삭제</a>
		</td>
	</tr>
	</c:forEach>
</table>

<!-- 댓글 폼 -->
<c:url var="urlCommentCreate" value="/board/${content.id}/comment/create"/>
<form:form method="post" commandName="comment" action="${urlCommentCreate}" class="comment">
	<input type="hidden" name="parent" value="${comment.parent.id}"/>
	<form:input path="content"/>
	<form:errors path="content"/>
	<input type="submit" value="입력"/>
</form:form >

<a href="<c:url value="/board/form/${content.id}"/>" class="link-param">수정</a>
<a href="<c:url value="/board/delete/${content.id}"/>" class="link-param">삭제</a>
<a href="<c:url value="/board/list"/>" class="link-param">리스트</a>
