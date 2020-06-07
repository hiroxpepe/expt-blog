<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="tab-content">
    <ul uk-tab>
        <li><a href="#tab1"><fmt:message key="label.list" /></a></li>
        <li><a href="#tab2"><fmt:message key="label.setting" /></a></li>
    </ul>
    <%-- #tab1 --%>
    <div id="tab1" class="tab">
        <div
            id="entry-list-block"
            class="entry-list-content">
        </div>
    </div>
    <%-- #tab2 --%>
    <div id="tab2" class="tab">
        <div class="setting-content">
            <div class="uk-grid">

                <div class="uk-width-1-4">
                    <label class="uk-form-label" for="entry_username">
                        <fmt:message key="entry.form.username" />
                    </label>
                </div>
                <div class="uk-width-3-4">
                    <form:input 
                        class="uk-input" 
                        id="entry_username" 
                        path="username" 
                    />
                </div>

                <div class="uk-width-1-4 uk-margin-top">
                    <label class="uk-form-label" for="entry_password">
                        <fmt:message key="entry.form.password" />
                    </label>
                </div>
                <div class="uk-width-3-4 uk-margin-top">
                    <form:password 
                        class="uk-input"
                        id="entry_password" 
                        path="password" 
                        showPassword="true" 
                    />
                </div>

                <div class="uk-width-1-4 uk-margin-top">
                    <label class="uk-form-label" for="entry_email">
                        <fmt:message key="entry.form.email" />
                    </label>
                </div>
                <div class="uk-width-3-4  uk-margin-top">
                    <form:input 
                        class="uk-input"
                        id="entry_email" 
                        path="email" 
                    />
                </div>

                <fmt:message key="button.setting" var="setting" />
                <div class="setting-command-block uk-margin-top">
                    <input id="setting-button"
                        class="command-button uk-button uk-button-primary"
                        type="button"
                        value="${setting}"
                    />
                </div>

            </div>
        </div>
    </div>
</div>
