<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="block permalink-content">
    <div class="permalink-entry-title">${entryModel.title}</div>
    <div class="permalink-entry-content">${entryModel.content}</div>
    <div class="permalink-entry-detail">
        <span>detail</span>
        <span class="permalink-entry-author">${entryModel.author} in </span>
        <span class="permalink-entry-category">${entryModel.category}</span>
        <span class="permalink-entry-date">
            <fmt:formatDate value="${entryModel.created}" pattern="yyyy-MM-dd (E) HH:mm:ss" />
        </span>
    </div>
    <div class="permalink-entry-tags">
        <span>tags</span>
        <c:set var="tags" value="${entryModel.tags}" />
        <c:forEach var="tag" items="${fn:split(tags, ' ')}">
            <span class="permalink-entry-tag">${tag}</span>
        </c:forEach>
    </div>
</div>