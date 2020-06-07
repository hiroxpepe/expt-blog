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
export default class EntryListTransformer {
    
    ///////////////////////////////////////////////////////////////////////////
    // public methods
    
    transform(obj) {
        console.log("/functor/htmltag/EntryListTransformer#transform");
        
        // dynamically generate an html table.
        let table = "<table class='entry-list-table'>";
        for (let i = 0; i < obj.entryModelList.length; i++) {
            
            // get the value
            let id = obj.entryModelList[i].id;  
            let code = obj.entryModelList[i].code;  
            let title = $.erasureHTML(
                obj.entryModelList[i].title
            );
            let content = $.erasureHTML(
                obj.entryModelList[i].content
            );
            let category = $.erasureHTML(
                obj.entryModelList[i].category
            );
            let tags = $.erasureHTML(
                obj.entryModelList[i].tags
            );
            let permalinkUrl = obj.entryModelList[i].permalinkUrl;
            let username = obj.entryModelList[i].username;
            let email = obj.entryModelList[i].email;
            let hash = 0;
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
                        "<span id='entry-content-" + code + "'>" + content.substring(0, 192) + "..." + "</span>" +
                        "<input id='entry-id-" + code + "' type='hidden' value='" + id + "' />" +
                        "<input id='entry-category-" + code + "' type='hidden' value='" + category + "' />" +
                        "<input id='entry-tags-" + code + "' type='hidden' value='" + tags + "' />" +
                    "</td>" +
                    "<td class='entry-action-td'>" +
                        "<table>" + 
                            "<tr>" +
                                "<td>" + 
                                    "<div id='entry-edit-" + code + "'" + " class='entry-action uk-button-primary' uk-tooltip='Edit'>e</div>" + 
                                 "</td>" +
                            "</tr>" +
                            "<tr>" + 
                                "<td>" + 
                                    "<div id='entry-delete-" + code + "'" + " class='entry-action uk-button-danger' uk-tooltip='Delete'>d</div>" + 
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
