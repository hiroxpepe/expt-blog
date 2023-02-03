<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- <div class="block menu-content">
    <div class="menu-bar"> -->
        <div class="menu-bar-left uk-navbar-left">
            <ul class="uk-navbar-nav">
                <li>
                    <a href="<c:url value='/' />"><fmt:message key="index.title" /></a>
                </li>
                <li>
                    <a href="<c:url value='/entry/form.html' />"><fmt:message key="button.entry" /></a>
                </li>
            </ul>
        </div>
        <div class="menu-bar-right uk-navbar-right">
            <ul class="uk-navbar-nav">
                <li>
                    <a class="feed" href="<c:url value='/entry/feed.rss' />" target="_blank">RSS</a>
                </li>
                <li>
                    <a class="feed" href="<c:url value='/entry/feed.atom' />" target="_blank">Atom</a>
                </li>
            </ul>
        </div>
    <!-- </div>
</div> -->
