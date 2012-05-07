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
import java.util.Set
import javax.inject.Inject

import org.apache.commons.collections.Factory
import org.dozer.Mapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext

import org.examproject.blog.dto.EntryDto
import org.examproject.blog.entity.Entry
import org.examproject.blog.entity.Paragraph
import org.examproject.blog.repository.EntryRepository
import org.examproject.blog.repository.ParagraphRepository

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
    private val mapper: Mapper = null
    
    @Inject
    private val repository: EntryRepository = null

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

    private def searchAllEntry(): List[EntryDto] = {
        
        // create the new dto list.
        val dtoList: List[EntryDto] = new ArrayList[EntryDto]()
        
        // get the entities list from repository.
        val list: List[Entry] = repository.findAll()
        for (entry: Entry <- list) {
            
            // map the object.
            val dto: EntryDto = context.getBean(classOf[EntryDto])            
            dto.setId(entry.getId)
            dto.setUsername(entry.getUser.getUsername())
            dto.setPassword(entry.getUser.getPassword())
            dto.setAuthor(entry.getAuthor())
            dto.setTitle(entry.getTitle())
            dto.setContent(entry.getContent())
            dto.setCategory("xxx")
            dto.setTags("xxx")
            dto.setCreated(entry.getCreated())
            dto.setCode(entry.getCode())
            
            // add to dto list.
            dtoList.add(dto)
        }
        return dtoList
    }

}
