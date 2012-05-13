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

package org.examproject.blog.util

import java.util.Set
import javax.inject.Inject

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

import org.examproject.blog.dto.EntryDto
import org.examproject.blog.entity.Entry
import org.examproject.blog.entity.Paragraph
import org.examproject.blog.entity.TagItem
import org.examproject.blog.repository.EntryRepository
import org.examproject.blog.repository.ParagraphRepository
import org.examproject.blog.repository.TagItemRepository

import scala.collection.JavaConversions._

/**
 * @author hiroxpepe
 */
@Component
class EntryUtils {
    
    private val LOG: Logger = LoggerFactory.getLogger(
        classOf[EntryUtils]
    )

    @Inject
    private val context: ApplicationContext = null
   
    @Inject
    private val entryRepository: EntryRepository = null

    @Inject
    private val paragraphRepository: ParagraphRepository = null
    
    @Inject
    private val tagItemRepository: TagItemRepository = null
    
    @Inject
    private val subjectUtils: SubjectUtils = null
    
    @Inject
    private val userUtils: UserUtils = null
    
    ///////////////////////////////////////////////////////////////////////////
    // public methods
    
    def getEntry(
        entryDto: EntryDto
    )
    : Entry = {
        try {
            if (entryDto.getId() == null) {
                val entry = context.getBean(classOf[Entry])
                entry.setUser(userUtils.getUser(entryDto))
                entry.setSubject(subjectUtils.getDefaultSubject(entryDto))
                LOG.debug("create entry.")
                return entry
            } else {
                val entry = entryRepository.findOne(entryDto.getId()).asInstanceOf[Entry]
                LOG.debug("update entry.")
                return entry
            }
        } catch {
            case e: Exception => {
                throw new RuntimeException("an error occurred.", e)
            }
        }
    }
    
    def saveEntity(
        entry: Entry
    ) {
        LOG.debug("called save.")
        
        if (entry.getId() == null) {
            entryRepository.save(entry)
            val paragraphSet: Set[Paragraph] = entry.getParagraphSet()
            for (paragraph: Paragraph <- paragraphSet) {
                paragraphRepository.save(paragraph)
            }
            val tagItemSet: Set[TagItem] = entry.getTagItemSet()
            for (tagItem: TagItem <- tagItemSet) {
                tagItemRepository.save(tagItem)
            }
        }
        else if (entry.getId() != null) {
            val paragraphSet: Set[Paragraph] = entry.getParagraphSet()
            for (paragraph: Paragraph <- paragraphSet) {
                paragraphRepository.save(paragraph)
            }
            val tagItemSet: Set[TagItem] = entry.getTagItemSet()
            for (tagItem: TagItem <- tagItemSet) {
                tagItemRepository.save(tagItem)
            }
            entryRepository.save(entry)
        }
    }
    
    def mapEntry(
        entry: Entry,
        dto: EntryDto
    )
    : EntryDto = {
        var title = ""
        var content = ""
        val paragraphSet: Set[Paragraph] =  entry.getParagraphSet()
        for (paragraph: Paragraph <- paragraphSet) {
            if (paragraph.getKey.equals("title")) {
                title = paragraph.getContent()
            }
            else if (paragraph.getKey.equals("content")) {
                content += paragraph.getContent()
            }
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
        
        return dto
    }
    
}
