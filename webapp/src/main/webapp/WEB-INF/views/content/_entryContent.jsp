<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="entry-content">
    <div class="entry-table uk-form-stacked">
        <fieldset class="uk-fieldset">

            <legend class="uk-legend">
                <fmt:message key="label.entry" />
            </legend>

            <fmt:message key="entry.form.title" var="title" />
            <div class="uk-margin">
                <form:input
                    class="uk-input"
                    id="entry-title"
                    path="title"
                    placeholder="${title}"
                />
            </div>

            <fmt:message key='entry.form.content' var="content" />
            <div class="uk-margin">
                <form:textarea
                    class="uk-textarea"
                    id="entry-content"
                    path="content"
                    rows="5"
                    placeholder="${content}"
                />
            </div>

            <fmt:message key="entry.form.tags" var="tags" />
            <div class="uk-margin">
                <form:input
                    class="uk-input"
                    id="entry-tags"
                    path="tags"
                    placeholder="${tags}"
                />
            </div>

            <div class="uk-margin">
                <form:select
                    class="uk-select"
                    id="entry-category"
                    path="category"
                    items="${categoryList}"
                />
            </td>

            <fmt:message key="button.save" var="save" />
            <div class="uk-margin">
                <input
                    class="command-button uk-button uk-button-primary"
                    id="post-button"
                    type="button"
                    value="${save}"
                />
            </div>

        </fieldset>
    </div>
</div>
