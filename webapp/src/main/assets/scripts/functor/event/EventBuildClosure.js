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

import { EditEventClosure } from '../event/EditEventClosure';
import { DeleteEventClosure } from '../event/DeleteEventClosure';

///////////////////////////////////////////////////////////////////////////////
/**
 * a functor class of the application.
 * build the event handler.
 * 
 * @author h.adachi
 */
export class EventBuildClosure {
    
    ///////////////////////////////////////////////////////////////////////////
    // public methods
    
    execute(obj) {
        
        const editEventClosure = new EditEventClosure();
        
        const deleteEventClosure = new DeleteEventClosure();
        
        for (let i = 0; i < obj.entryModelList.length; i++) {
            let code = obj.entryModelList[i].code;
            
            // set the event handler for edit.
            editEventClosure.execute({
                code: code
            });
            
            // set the event handler for delete.
            deleteEventClosure.execute({
                code: code
            });
        }
    }
}
