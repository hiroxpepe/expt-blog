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
import java.util.HashSet
import javax.inject.Inject

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

import org.examproject.blog.dto.EntryDto
import org.examproject.blog.entity.Entry
import org.examproject.blog.entity.Tag
import org.examproject.blog.entity.TagItem
import org.examproject.blog.repository.TagRepository
import org.examproject.blog.repository.TagItemRepository

import scala.collection.JavaConversions._

/**
 * @author hiroxpepe
 */
@Component
class TagUtils {
    
    private val LOG: Logger = LoggerFactory.getLogger(
        classOf[TagUtils]
    )
    
    @Inject
    private val context: ApplicationContext = null
    
    @Inject
    private val tagRepository: TagRepository = null
    
    @Inject
    private val tagItemRepository: TagItemRepository = null
    
    ///////////////////////////////////////////////////////////////////////////
    // public methods
    
    ///////////////////////////////////////////////////////////////////////////
    /**
     * get the default tag.
     */
    def getDefaultTag()
    : Tag = {
        try {
            return tagRepository.findByText("default")
        } catch {
            case e: Exception => {
                throw new RuntimeException("an error occurred.", e)
            }
        }
    }
    
    ///////////////////////////////////////////////////////////////////////////
    /**
     * get the default tag item set.
     */
    def getDefaultTagItemSet(
        entry: Entry
    )
    : Set[TagItem] = {
        try {
            val tag: Tag = getDefaultTag()
            val tagItem: TagItem = context.getBean(classOf[TagItem])
            tagItem.setEntry(entry)
            tagItem.setTag(tag)
            val tagItemSet: Set[TagItem] = new HashSet[TagItem]
            tagItemSet.add(tagItem)
            return tagItemSet
        } catch {
            case e: Exception => {
                throw new RuntimeException("an error occurred.", e)
            }
        }
    }
    
    ///////////////////////////////////////////////////////////////////////////
    /**
     * get the tag item set.
     */
    def getTagItemSet(
        entryDto: EntryDto,
        entry: Entry
    )
    : Set[TagItem] = {
        try {
            ///////////////////////////////////////////////////////////////////
            // if the entry is new one.
            if (entry.getId() == null) {
                
                // create the tag items for entity.
                return getNewTagItemSet(
                    entryDto,
                    entry
                )
            }
            ///////////////////////////////////////////////////////////////////
            // if entry is updated.
            
            // delete the tag items of entity.
            val tagItemSet: Set[TagItem] = entry.getTagItemSet()
            for (tagItem: TagItem <- tagItemSet) {
                tagItemRepository.delete(tagItem.getId())
            }
            
            // create the tag items for entity.
            return getNewTagItemSet(
                entryDto,
                entry
            )
        } catch {
            case e: Exception => {
                throw new RuntimeException("an error occurred.", e)
            }
        }
    }
    
    ///////////////////////////////////////////////////////////////////////////
    /**
     * get the tag item set.
     */
    def getTagItemString(
        entry: Entry
    ) 
    : String = {
        try {
            // get the tag items of entity.
            val builder = new StringBuilder()
            val tagItemSet: Set[TagItem] = entry.getTagItemSet()
            for (tagItem: TagItem <- tagItemSet) {
                val tag: Tag = tagRepository.findOne(tagItem.getTag.getId())
                builder.append(tag.getText())
                builder.append(" ")
            }
            builder.toString()
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
     * get the new tag item set.
     */
    private def getNewTagItemSet(
        entryDto: EntryDto,
        entry: Entry
    )
    : Set[TagItem] = {
        try {
            val tagItemSet: Set[TagItem] = new HashSet[TagItem]()
            val tags = entryDto.getTags.split(" ")
            for (tagString: String <- tags) {
                // to lowercase.
                val lowerTagString = tagString.map(
                    c => c.toLower
                )
                // confirm the existence.
                val tag: Tag = tagRepository.findByText(lowerTagString)
                // create a new tag.
                if (tag == null) {
                    val newTag = context.getBean(classOf[Tag])
                    newTag.setText(lowerTagString)
                    tagRepository.save(newTag)
                    LOG.debug("create the new tag.")
                    val tagItem: TagItem = context.getBean(classOf[TagItem])
                    tagItem.setEntry(entry)
                    tagItem.setTag(newTag)
                    tagItemSet.add(tagItem)     
                // if already exist set this.
                } else {    
                    val tagItem: TagItem = context.getBean(classOf[TagItem])
                    tagItem.setEntry(entry)
                    tagItem.setTag(tag)
                    tagItemSet.add(tagItem)
                }
            }
            return tagItemSet
        } catch {
            case e: Exception => {
                throw new RuntimeException("an error occurred.", e)
            }
        }
    }
    
}