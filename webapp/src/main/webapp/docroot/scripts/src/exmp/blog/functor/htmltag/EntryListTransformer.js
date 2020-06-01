/* 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

///////////////////////////////////////////////////////////////////////////////
/**
 * a functor class of the application.
 * this class is a transformer that JSON data get by
 * Ajax HTTP requests and convert to HTML tables.
 * 
 * @author h.adachi
 */
exmp.blog.functor.htmltag.EntryListTransformer = {
    
    ///////////////////////////////////////////////////////////////////////////
    // public methods
    
    transform: function(obj) {
        console.log("exmp.blog.functor.htmltag.EntryListTransformer#transform");
                
        // dynamically generate an html table.
        var table = "<table class='entry-list-table'>";
        for (var i = 0; i < obj.entryModelList.length; i++) {
            
            // get the value
            var id = obj.entryModelList[i].id;  
            var code = obj.entryModelList[i].code;  
            var title = $.erasureHTML(
                obj.entryModelList[i].title
            );
            var content = $.erasureHTML(
                obj.entryModelList[i].content
            );
            var category = $.erasureHTML(
                obj.entryModelList[i].category
            );
            var tags = $.erasureHTML(
                obj.entryModelList[i].tags
            );
            var permalinkUrl = obj.entryModelList[i].permalinkUrl;
            var username = obj.entryModelList[i].username;
            var email = obj.entryModelList[i].email;
            var hash = 0;
            if (email) { hash = MD5_hexhash(email); }
            
            // create an html tag and set the entry code.
            // TODO: another user's e and d..
            table +=
                "<tr>" +
                    "<td class='entry-icon-td'>" + 
                        "<div class='entry-icon'>" + 
                            "<img src='http://2.gravatar.com/avatar/" +
                                hash + "' width='48' height='48' border='0'>" +
                        "</div>" +
                    "</td>" +
                    "<td class='entry-list-td' >" +
                        "<a href='" + permalinkUrl + "'>" + 
                            "<span id='entry-title-" + code + "'>" + title + "</span>" + 
                        "</a>" + " " +
                        "<span id='entry-content-" + code + "'>" + content + "</span>" +
                        "<input id='entry-id-" + code + "' type='hidden' value='" + id + "' />" +
                        "<input id='entry-category-" + code + "' type='hidden' value='" + category + "' />" +
                        "<input id='entry-tags-" + code + "' type='hidden' value='" + tags + "' />" +
                    "</td>" +
                    "<td class='entry-action-td'>" +
                        "<table>" + 
                            "<tr>" +
                                "<td>" + 
                                    "<div id='entry-edit-" + code + "'" + " class='entry-action'>e</div>" + 
                                 "</td>" +
                            "</tr>" +
                            "<tr>" + 
                                "<td>" + 
                                    "<div id='entry-delete-" + code + "'" + " class='entry-action'>d</div>" + 
                                 "</td>" +
                            "</tr>" +
                        "</table>" +
                    "</td>" +
                "</tr>";
        }
        table += "</table>";
        return table;
    }
}
