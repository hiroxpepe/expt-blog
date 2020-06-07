<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta http-equiv="Content-Language" content="en">
        <link rel="shortcut icon" href="<c:url value='/docroot/images/icon.ico'/>" />
        <link rel="stylesheet" type="text/css" href="<c:url value='/docroot/styles/jquery-ui.custom.css'/>" />
        <script language="javascript" type="text/javascript" src="<c:url value='/docroot/scripts/jquery.js'/>"></script>
        <script language="javascript" type="text/javascript" src="<c:url value='/docroot/scripts/jquery-ui.custom.min.js'/>"></script>
        <script language="javascript" type="text/javascript" src="<c:url value='/docroot/scripts/jquery.json.js'/>"></script>
        <script language="javascript" type="text/javascript" src="<c:url value='/docroot/scripts/jquery.escapeHTML.js'/>"></script>
        <script language="javascript" type="text/javascript" src="<c:url value='/docroot/scripts/md5.js'/>"></script>
        <script language="javascript" type="text/javascript" src="<c:url value='/docroot/scripts/exmp-blog.min.js'/>"></script>
        <title><fmt:message key="site.title" /></title>
    </head>
    <body>
        <div class="container uk-container uk-container-center">
            <div class="menu uk-navbar uk-navbar-container" data-uk-sticky="">
                <tiles:insertAttribute name="menu" />
            </div>
            <div class="header uk-section uk-section-primary">
                <tiles:insertAttribute name="header" />
            </div>
            <div class="uk-grid uk-grid-match">
                <div class="content uk-width-2-3 uk-margin-remove-left">
                    <tiles:insertAttribute name="content" />
                </div>
                <div class="sidebar uk-width-1-3 uk-padding-remove-left">
                    <tiles:insertAttribute name="sidebar"/>
                </div>
            </div>
            <div class="footer uk-section uk-section-primary">
                <tiles:insertAttribute name="footer" />
            </div>
        </div>
    </body>
</html>
