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

import org.apache.commons.collections.Closure
import org.apache.commons.collections.Factory
import org.apache.commons.collections.Transformer

import org.examproject.blog.dto.EntryDto
import org.examproject.blog.service.EntryService

import scala.collection.JavaConversions._

/**
 * @author hiroxpepe
 */
class SimpleEntryService(
    val idToEntryTransformer: Transformer,
    val codeToEntryTransformer: Transformer,
    val allEntryFactory: Factory,
    val saveEntryClosure: Closure,
    val deleteEntryClosure: Closure
) extends EntryService {
    
    // there is nothing of a logic here, it's just an adapter class..
    
    @Override
    def getEntryById(id: Long): EntryDto = {
        return idToEntryTransformer.transform(id).asInstanceOf[EntryDto]
    }
    
    @Override
    def getEntryByCode(code: String): EntryDto = {
        return codeToEntryTransformer.transform(code).asInstanceOf[EntryDto]
    }

    @Override
    def findAllEntry(): List[EntryDto] = {
        return allEntryFactory.create().asInstanceOf[List[EntryDto]]
    }

    @Override
    def saveEntry(entryDto: EntryDto) {
        saveEntryClosure.execute(entryDto)
    }

    @Override
    def deleteEntry(entryDto: EntryDto) {
        deleteEntryClosure.execute(entryDto)
    }

}
