<%@ tag display-name="Pagination tag" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="pageResponse" type="io.teamscala.java.core.data.PageResponse" description="The paginate response" required="true" %>
<%@ attribute name="qualifier" type="java.lang.String" description="The qualifier" required="false" %>

<c:if test="${pageResponse.totalCount gt 0}">
	<c:set var="linkPageClassName" value="link-page"/>
	<c:if test="${not empty qualifier}">
		<c:set var="linkPageClassName" value="${qualifier}-${linkPageClassName}"/>
	</c:if>
	<c:if test="${pageResponse.notFirstPage}"><a href="#" rel="${pageResponse.firstPage}" class="${linkPageClassName}">[처음]</a></c:if>
	<c:if test="${pageResponse.hasPrevBound}"><a href="#" rel="${pageResponse.endPageOfPrevBound}" class="${linkPageClassName}">[이전]</a></c:if>
	<c:forEach var="page" begin="${pageResponse.beginPage}" end="${pageResponse.endPage}">
		<c:choose>
			<c:when test="${pageResponse.currentPage eq page}"><strong>${page}</strong></c:when>
			<c:otherwise><a href="#" rel="${page}" class="${linkPageClassName}">${page}</a></c:otherwise>
		</c:choose>
	</c:forEach>
	<c:if test="${pageResponse.hasNextBound}"><a href="#" rel="${pageResponse.beginPageOfNextBound}" class="${linkPageClassName}">[다음]</a></c:if>
	<c:if test="${pageResponse.notLastPage}"><a href="#" rel="${pageResponse.lastPage}" class="${linkPageClassName}">[끝]</a></c:if>
</c:if>