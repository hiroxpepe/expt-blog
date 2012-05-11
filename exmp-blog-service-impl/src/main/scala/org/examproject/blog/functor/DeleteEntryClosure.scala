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

import java.util.Set
import javax.inject.Inject

import org.apache.commons.collections.Closure
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional

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
class DeleteEntryClosure extends Closure {
    
    private val LOG: Logger = LoggerFactory.getLogger(
        classOf[DeleteEntryClosure]
    )
    
    @Inject
    private val repository: EntryRepository = null
    
    @Inject
    private val paragraphRepository: ParagraphRepository = null
    
    @Inject
    private val tagItemRepository: TagItemRepository = null
    
    ///////////////////////////////////////////////////////////////////////////
    // public methods
    
    @Override
    def execute(o: Object) {
        LOG.debug("called.")
        val entryDto: EntryDto = o.asInstanceOf[EntryDto]
        try {
            delete(entryDto)
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
    private def delete(
        entryDto: EntryDto
    ) {
        try {
            // to search the repository for delete.
            val entry: Entry = repository.findOne(entryDto.getId()).asInstanceOf[Entry]
            
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
            repository.delete(entry.getId())
            
        } catch {
            case e: Exception => {
                throw new RuntimeException("delete failed. ", e)
            }
        }
    }

}
