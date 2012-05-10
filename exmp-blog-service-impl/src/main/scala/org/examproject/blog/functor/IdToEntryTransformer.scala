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

import java.lang.Long
import java.util.Set
import javax.inject.Inject

import org.apache.commons.collections.Transformer
import org.dozer.Mapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import org.springframework.transaction.annotation.Transactional

import org.examproject.blog.dto.EntryDto
import org.examproject.blog.entity.Entry
import org.examproject.blog.entity.Paragraph
import org.examproject.blog.repository.EntryRepository

import scala.collection.JavaConversions._

/**
 * @author hiroxpepe
 */
class IdToEntryTransformer extends Transformer {

    private val LOG: Logger = LoggerFactory.getLogger(
        classOf[IdToEntryTransformer]
    )

    @Inject
    private val context: ApplicationContext = null
    
    @Inject
    private val mapper: Mapper = null
    
    @Inject
    private val repository: EntryRepository = null

    ///////////////////////////////////////////////////////////////////////////
    // public methods
    
    @Override
    def transform(o: Object ): Object = {
        LOG.debug("called.")
        try {
            return getEntryDto(o)
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
    private def getEntryDto(
        o: Object
    )
    : EntryDto = {
        
        // if 'id' is offered, find the entity from repository, and mapping to dto.
        if (o != null) {
            // get the entity from repository.
            val id: Long = Long.valueOf(o.toString())
            val entry: Entry = repository.findOne(id).asInstanceOf[Entry]
            
            // map entity to dto.
            val dto: EntryDto = context.getBean(classOf[EntryDto])
            mapEntry(entry, dto)
              
            // return a mapped dto.
            return dto
            
        // if the new request, a null 'id' will be provided.
        } else {
            // return a new dto.
            return context.getBean(classOf[EntryDto])
        }
    }
    
    private def mapEntry(
        entry: Entry,
        dto: EntryDto
    ) = {
        var title = ""
        var content = ""
        val paragraphSet: Set[Paragraph] =  entry.getParagraphSet()
        for (paragraph: Paragraph <- paragraphSet) {
            if (paragraph.getKind.equals("title")) {
                title = paragraph.getContent()
            }
            content += paragraph.getContent()
        }
        
        // map the object.       
        dto.setId(entry.getId)
        dto.setUsername(entry.getUser.getUsername())
        dto.setPassword(entry.getUser.getPassword())
        dto.setAuthor(entry.getAuthor())
        dto.setTitle(title)
        dto.setContent(content)
        dto.setCategory("xxx")
        dto.setTags("xxx")
        dto.setCreated(entry.getCreated())
        dto.setCode(entry.getCode())
    }
    
}