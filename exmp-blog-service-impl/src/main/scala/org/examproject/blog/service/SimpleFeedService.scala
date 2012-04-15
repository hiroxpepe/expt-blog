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

package org.examproject.blog.service

import java.lang.Long
import java.util.List

import org.apache.commons.collections.Factory

import org.examproject.blog.dto.EntryDto
import org.examproject.blog.service.FeedService

import scala.collection.JavaConversions._

/**
 * @author hiroxpepe
 */
class SimpleFeedService(
    val allEntryFactory: Factory
) extends FeedService {
    
    // there is nothing of a logic here, it's just an adapter class..
    
    @Override
    override def findAllEntry(): List[EntryDto] = {
        return allEntryFactory.create().asInstanceOf[List[EntryDto]]
    }
}
