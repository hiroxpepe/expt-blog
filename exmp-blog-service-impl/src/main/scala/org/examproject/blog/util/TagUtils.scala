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
import java.util.HashSet
import javax.inject.Inject

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

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
    
    def getTagItemSet(
        entry: Entry
    )
    : Set[TagItem] = {
        try {
            val tagItemList: List[TagItem] = tagItemRepository.findByEntry(entry)
            val tagItemSet: Set[TagItem] = new HashSet[TagItem]()
            for (tagItem: TagItem <- tagItemList) {
                tagItemSet.add(tagItem)
            }
            return tagItemSet
        } catch {
            case e: Exception => {
                throw new RuntimeException("an error occurred.", e)
            }
        }
    }
    
}