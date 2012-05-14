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

import java.util.List
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
import org.examproject.blog.entity.User
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
    private val paragraphUtils: ParagraphUtils = null
    
    @Inject
    private val subjectUtils: SubjectUtils = null
    
    @Inject
    private val tagUtils: TagUtils = null
    
    @Inject
    private val userUtils: UserUtils = null
    
    ///////////////////////////////////////////////////////////////////////////
    // public methods
    
    ///////////////////////////////////////////////////////////////////////////
    /**
     * get the entry dto.
     */
    def getEntry(
        entryDto: EntryDto
    )
    : Entry = {
        try {
            // create the entity if the dto has no id.
            if (entryDto.getId() == null) {
                val user: User = userUtils.getUser(entryDto)
                
                // find the title...
                val title: String = entryDto.getTitle()
                val entryList: List[Entry] = entryRepository.findByUser(user)
                for (entry: Entry <- entryList) {
                    val paragraphSet: Set[Paragraph] = entry.getParagraphSet();
                    for (paragraph: Paragraph <- paragraphSet) {
                        if (paragraph.getKey.equals("title")) {
                            if (paragraph.getContent.equals(title)) {
                                LOG.debug("found the entry.")
                                return paragraph.getEntry()
                            }
                        }
                    }
                }
                
                // cannot find the title.
                val entry = context.getBean(classOf[Entry])
                entry.setUser(user)
                LOG.debug("create the entry.")
                return entry
                
            // edit the entity if the dto has id.
            } else {
                val entry = entryRepository.findOne(
                    entryDto.getId()
                ).asInstanceOf[Entry]
                LOG.debug("update the entry.")
                return entry
            }
        } catch {
            case e: Exception => {
                throw new RuntimeException("an error occurred.", e)
            }
        }
    }
    
    ///////////////////////////////////////////////////////////////////////////
    /**
     * get the entry entity.
     */
    def saveEntry(
        entry: Entry
    ) {
        try {
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
        } catch {
            case e: Exception => {
                throw new RuntimeException("an error occurred.", e)
            }
        }
    }
    
    ///////////////////////////////////////////////////////////////////////////
    /**
     * map the entry entity to the entry dto.
     */
    def mapEntry(
        entry: Entry,
        entryDto: EntryDto
    )
    : EntryDto = {
        try {            
            // map the entity to the dto.
            entryDto.setId(entry.getId)
            entryDto.setUsername(entry.getUser.getUsername())
            entryDto.setPassword(entry.getUser.getPassword())
            entryDto.setEmail(entry.getUser.getEmail())
            entryDto.setAuthor(entry.getAuthor())
            entryDto.setTitle(paragraphUtils.getTitleString(entry))
            entryDto.setContent(paragraphUtils.getContentString(entry))
            entryDto.setSubject(entry.getSubject().getText())
            entryDto.setCategory(subjectUtils.getCategoryString(entry))
            entryDto.setTags(tagUtils.getTagItemString(entry))
            entryDto.setCreated(entry.getCreated())
            entryDto.setEntryCode(entry.getCode())

            return entryDto
        } catch {
            case e: Exception => {
                throw new RuntimeException("an error occurred.", e)
            }
        }
    }
    
    ///////////////////////////////////////////////////////////////////////////
    /**
     * map the entry dto to the entry entity.
     */
    def mapEntry(
        entryDto: EntryDto,
        entry: Entry
    )
    : Entry = {
        try {            
            // map the dto value to the entity.
            entry.setAuthor(entryDto.getAuthor())
            entry.setParagraphSet(paragraphUtils.getParagraphSet(entryDto, entry))   
            entry.setTagItemSet(tagUtils.getTagItemSet(entryDto, entry))
            entry.setCreated(entryDto.getCreated())
            entry.setUpdated(entryDto.getCreated())
            entry.setCode(entryDto.getEntryCode())
            entry.setSubject(subjectUtils.getSubject(entryDto))
            return entry
        } catch {
            case e: Exception => {
                throw new RuntimeException("an error occurred.", e)
            }
        }
    }
    
    ///////////////////////////////////////////////////////////////////////////
    /**
     * delete the entry entity.
     */
    def deleteEntry(
        entryDto: EntryDto
    ) {
        try {
            // to search the repository for delete.
            val entry: Entry = entryRepository.findOne(
                entryDto.getId()
            ).asInstanceOf[Entry]
            
            // delete the entry's paragraphs.
            val paragraphSet: Set[Paragraph] = entry.getParagraphSet()
            for (paragraph: Paragraph <- paragraphSet) {
                paragraphRepository.delete(paragraph.getId())
            }
            
            // delete the entry's tagitems.
            val tagItemSet: Set[TagItem] = entry.getTagItemSet()
            for (tagItem: TagItem <- tagItemSet) {
                tagItemRepository.delete(tagItem.getId())
            }
            
            // delete the entry.
            entryRepository.delete(entry.getId())
        } catch {
            case e: Exception => {
                throw new RuntimeException("an error occurred.", e)
            }
        }
    }
    
}
