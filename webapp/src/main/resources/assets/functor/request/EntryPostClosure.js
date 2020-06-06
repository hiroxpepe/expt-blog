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

import WaitingMessageClosure from '../dhtml/WaitingMessageClosure';
import SuccessMessageClosure from '../dhtml/SuccessMessageClosure';
import ErrorMessageClosure from '../dhtml/ErrorMessageClosure';
import EntryListUpdateClosure from '../dhtml/EntryListUpdateClosure';
import EventBuildClosure from '../event/EventBuildClosure';

///////////////////////////////////////////////////////////////////////////////
/**
 * a functor class of the application.
 * send HTTP request for the entry post.
 * 
 * @author h.adachi
 */
export default class EntryPostClosure {
    
    ///////////////////////////////////////////////////////////////////////////
    // public methods
    
    execute(obj) {
        
        const waitingMessageClosure = new WaitingMessageClosure();
        
        const successMessageClosure = new SuccessMessageClosure();
        
        const errorMessageClosure = new ErrorMessageClosure();
        
        const entryListUpdateClosure = new EntryListUpdateClosure();
        
        const eventBuildClosure = new EventBuildClosure();
        
        // show the waiting message.
        waitingMessageClosure.execute({
            message: "please wait..."
        });
        
        // create an ajax object.
        new $.ajax({
            url: "post.json",
            type: "POST",
            data: obj,
            dataType: "json",
            contentType: "application/json;charset=UTF-8",
            
            // callback function of the success.
            success: function(data, dataType) {
                
                // if get a error from the response.
                if (data.isError) {
                    // show the error message.
                    errorMessageClosure.execute({
                        message: "application error occurred.."
                    });
                    return;
                }
                
                // update the HTML table of the entry list.
                entryListUpdateClosure.execute(
                    data
                );
                    
                // build the event of the entry list.
                eventBuildClosure.execute(
                    data
                );
                
                // clear the input.
                $("#id").val("");
                $("#code").val("");
                $("#entry-title").val("");
                $("#entry-content").val("");
                $("#entry-category").val(0);
                $("#entry-tags").val("");
                
                // show the success message.
                successMessageClosure.execute({
                    message: "complete."
                });
            },
            
            // callback function of the error.
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                
                // show the error message.
                errorMessageClosure.execute({
                    message: "httprequest error occurred.."
                });
            }
        });
    }
}
