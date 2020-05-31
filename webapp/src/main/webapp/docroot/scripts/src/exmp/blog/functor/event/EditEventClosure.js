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
 * set the event handler of entry edit action.
 * 
 * @author h.adachi
 */
exmp.blog.functor.event.EditEventClosure = {
    
    ///////////////////////////////////////////////////////////////////////////
    // public methods
    
    execute: function(obj) {
        console.log("exmp.blog.functor.event.EditEventClosure#execute");
               
        // dynamically generate an event handler.
        $("#entry-edit-" + obj.code).click(function() {
                        
            // get the entry value for from that need to edit.
            $("#entry-title").val(
                $("#entry-title-" + obj.code).text()
            );
            $("#entry-content").val(
                $("#entry-content-" + obj.code).text()
            );
            $("#entry-category").val(
                $("#entry-category-" + obj.code).val()
            );
            $("#entry-tags").val(
                $("#entry-tags-" + obj.code).val()
            );
            $("#id").val(
                $("#entry-id-" + obj.code).val()
            );
            $("#code").val(
                obj.code
            );
        });
    }
}
