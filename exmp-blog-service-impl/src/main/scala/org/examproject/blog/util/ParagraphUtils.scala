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

import java.util.Date
import java.util.Set
import java.util.HashSet
import javax.inject.Inject

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

import org.examproject.blog.dto.EntryDto
import org.examproject.blog.entity.Entry
import org.examproject.blog.entity.Paragraph
import org.examproject.blog.repository.ParagraphRepository

import scala.collection.JavaConversions._

/**
 * @author hiroxpepe
 */
@Component
class ParagraphUtils {
    
    private val LOG: Logger = LoggerFactory.getLogger(
        classOf[ParagraphUtils]
    )

    @Inject
    private val context: ApplicationContext = null
        
    @Inject
    private val paragraphRepository: ParagraphRepository = null
    
    ///////////////////////////////////////////////////////////////////////////
    // public methods
    
    ///////////////////////////////////////////////////////////////////////////
    /**
     * get the paragraph set.
     */
    def getParagraphSet(
        entryDto: EntryDto,
        entry: Entry
    )
    : Set[Paragraph] = {
        try {
            val titleParagraph: Paragraph = getTitle(entryDto, entry)
            val contentParagraph: Paragraph = getContent(entryDto, entry)
            val paragraphSet: Set[Paragraph] = new HashSet[Paragraph]
            paragraphSet.add(titleParagraph)
            paragraphSet.add(contentParagraph)
            return paragraphSet
        } catch {
            case e: Exception => {
                throw new RuntimeException("an error occurred.", e)
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    /**
     * get the title string.
     */
    def getTitleString(
        entry: Entry
    )
    : String = {
        try {
            val paragraphSet: Set[Paragraph] =  entry.getParagraphSet()
            for (paragraph: Paragraph <- paragraphSet) {
                if (paragraph.getKey.equals("title")) {
                    return paragraph.getContent()
                }
            }
            return null
        } catch {
            case e: Exception => {
                throw new RuntimeException("an error occurred.", e)
            }
        }
    }
    
    ///////////////////////////////////////////////////////////////////////////
    /**
     * get the title string.
     */
    def getContentString(
        entry: Entry
    )
    : String = {
        try {
            val paragraphSet: Set[Paragraph] =  entry.getParagraphSet()
            for (paragraph: Paragraph <- paragraphSet) {
                if (paragraph.getKey.equals("content")) {
                    return paragraph.getContent()
                }
            }
            return null
        } catch {
            case e: Exception => {
                throw new RuntimeException("an error occurred.", e)
            }
        }
    }
    
    ///////////////////////////////////////////////////////////////////////////
    // private methods
    
    ///////////////////////////////////////////////////////////////////////////
    /**
     * get the title paragraph.
     */
    private def getTitle(
        entryDto: EntryDto,
        entry: Entry
    )
    : Paragraph = {
        try {
            if (entry.getId() == null) {
                val newTitle: Paragraph = context.getBean(classOf[Paragraph])
                newTitle.setContent(entryDto.getTitle())
                newTitle.setKey("title")
                newTitle.setCreated(new Date())
                newTitle.setUpdated(new Date())
                newTitle.setEntry(entry)
                return newTitle
            }
            val title: Paragraph = paragraphRepository.findByEntryAndKey(entry, "title")
            title.setContent(entryDto.getTitle())
            title.setUpdated(new Date())
            return title
        } catch {
            case e: Exception => {
                throw new RuntimeException("an error occurred.", e)
            }
        }
    }
    
    ///////////////////////////////////////////////////////////////////////////
    /**
     * get the content paragraph.
     */
    private def getContent(
        entryDto: EntryDto,
        entry: Entry
    )
    : Paragraph = {
        try {
            if (entry.getId() == null) {
                val newContent: Paragraph = context.getBean(classOf[Paragraph])
                newContent.setContent(entryDto.getContent())
                newContent.setKey("content")
                newContent.setCreated(new Date())
                newContent.setUpdated(new Date())
                newContent.setEntry(entry)
                return newContent
            }
            val content: Paragraph = paragraphRepository.findByEntryAndKey(entry, "content")
            content.setContent(entryDto.getContent())
            content.setUpdated(new Date())
            return content
        } catch {
            case e: Exception => {
                throw new RuntimeException("an error occurred.", e)
            }
        }
    }
    
}
