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

package org.examproject.blog.functor

import java.util.ArrayList
import java.util.List
import javax.inject.Inject

import org.apache.commons.collections.Factory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import org.springframework.transaction.annotation.Transactional

import org.examproject.blog.dto.EntryDto
import org.examproject.blog.entity.Entry
import org.examproject.blog.repository.EntryRepository
import org.examproject.blog.util.EntryUtils

import scala.collection.JavaConversions._

/**
 * @author hiroxpepe
 */
class AllEntryFactory extends Factory {

    private val LOG: Logger = LoggerFactory.getLogger(
        classOf[AllEntryFactory]
    )

    @Inject
    private val context: ApplicationContext = null
    
    @Inject
    private val entryRepository: EntryRepository = null
    
    @Inject
    private val entryUtils: EntryUtils = null

    ///////////////////////////////////////////////////////////////////////////
    // public methods
    
    @Override
    def create(): Object = {
        LOG.debug("called.")
        try {
            return searchAllEntry()
        } catch {
            case re: RuntimeException => {
                LOG.error("RuntimeException occurred. " + re.getMessage())
                throw re
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // private methods
    
    @Transactional
    private def searchAllEntry()
    : List[EntryDto] = {
        
        // create the new dto list.
        val dtoList: List[EntryDto] = new ArrayList[EntryDto]()
        
        // get the entities list from repository.
        val list: List[Entry] = entryRepository.findAll()
        for (entry: Entry <- list) {
            
            // map the object and add to dto list.
            val dto: EntryDto = context.getBean(classOf[EntryDto])
            dtoList.add(entryUtils.mapEntry(entry, dto))
        }
        return dtoList
    }

}