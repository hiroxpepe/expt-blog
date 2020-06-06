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
 * display a message to div when an ajax http request command waiting.
 * 
 * @author h.adachi
 */
export default class WaitingMessageClosure {
    
    ///////////////////////////////////////////////////////////////////////////
    // public methods
    
    execute(obj) {
        
        // update the html element and the css style.
        $("#message-block")
            .addClass(
                "show"
            )
            .css({
                margin: "1em 0.25em 1em 0.25em",
                padding: "0.25em",
                color: "silver",
                backgroundColor: "lavender",
                border: "1px solid silver"
            })
            .html(
                '<p><img src="../docroot/images/loading.gif" ' +
                    'width="31" height="31" /></p>' + 
                "<p>" + obj.message + "</p>"
            );
    }
}
