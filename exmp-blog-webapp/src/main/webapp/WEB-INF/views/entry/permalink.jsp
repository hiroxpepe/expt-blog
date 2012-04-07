<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="block index-content">
<p>date: <fmt:formatDate value="${entryModel.created}" pattern="yyyy-MM-dd HH:mm:ss" /></p>
<p>title: ${entryModel.title}</p>
<p>content: ${entryModel.content}</p>
</div>