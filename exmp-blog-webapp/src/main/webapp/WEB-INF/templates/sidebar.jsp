<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="block sidebar-content">
    <p><fmt:message key="label.select" /></p>
    <a href="<c:url value="/entry/form.html" />"><fmt:message key="button.entry" /></a>
    <a class="feed" href="<c:url value="/entry/feed.rss" />" target="_blank">RSS</a>
    <a class="feed" href="<c:url value="/entry/feed.atom" />" target="_blank">Atom</a>
</div>