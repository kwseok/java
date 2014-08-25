<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/views/commons/taglibs.jspf" %>

<script type="text/javascript">
define('body', ['configs', 'jquery'], function(configs, $){
	$.param.setDefaultParams({
		page: '${param.page}',
		condition: '${param.condition}',
		keyword: '${param.keyword}'
	});

    $('.link-page').click(function(e){
        e.preventDefault();
        var page = $(this).attr('rel');
        if (page > 0){
            location.search = $.param.queryString({ page: page });
        }
    });

    $('form.search').validate({
        rules: {
            keyword: {
                required: true,
                rangelength: [2, 10]
            }
        },
        messages: {
            keyword: {
                required: '검색어를 입력하세요.',
                rangelength: '검색어는 {0}자에서 {1}자까지 입력가능 합니다.'
            }
        }
    });
});
</script>

<table>
	<tr>
		<td>순번</td>
		<td>제목</td>
		<td>수정일</td>
		<td>등록일</td>
	</tr>
	<c:if test="${empty contents.list}">
	<tr>
		<td colspan="3" align="center">내용이 없습니다.</td>
	</tr>
	</c:if>
	<c:forEach var="content" items="${contents.list}" varStatus="status">
	<tr>
		<td>${contents.reverseNo - status.index}</td>
		<td>
			<a href="<c:url value="/board/view/${content.id}"/>" class="link-param">${content.title}</a>
		</td>
		<td>
			<fmt:formatDate value="${content.modDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
		</td>
		<td>
			<fmt:formatDate value="${content.regDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
		</td>
	</tr>
	</c:forEach>
</table>

<!-- 페이징 -->
[<tag:pagination pageResponse="${contents}"/>]

<!-- 검색 -->
<c:url var="urlList" value="/board/list"/>
<form:form method="get" commandName="boardSearch" action="${urlList}" class="search">
	<form:select path="condition">
		<form:option value="title">제목</form:option>
		<form:option value="content">내용</form:option>
		<form:option value="title,content">제목+내용</form:option>
	</form:select>
	<form:input path="keyword"/>
	<form:errors path="keyword"/>
	<input type="submit" value="검색"/>
	<c:if test="${not empty param.keyword}">
	<input type="button" value="전체" onclick="location='<c:url value="/board/list"/>';"/>
	</c:if>
</form:form>

<!-- 입력 -->
<a href="<c:url value="/board/form"/>">입력</a>