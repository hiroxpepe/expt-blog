<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="block menu-content">
    <div class="menu-bar">
        <div class="menu-bar-left">
            <span>
                <a href="<c:url value="/" />"><fmt:message key="index.title" /></a>
            </span>
            <span>
                <a href="<c:url value="/entry/form.html" />"><fmt:message key="button.entry" /></a>
            </span>
        </div>
        <div class="menu-bar-right">
            <span>
                <a class="feed" href="<c:url value="/entry/feed.rss" />" target="_blank">RSS</a>
            </span>
            <span>
                <a class="feed" href="<c:url value="/entry/feed.atom" />" target="_blank">Atom</a>
            </span>
        </div>
    </div>
</div>