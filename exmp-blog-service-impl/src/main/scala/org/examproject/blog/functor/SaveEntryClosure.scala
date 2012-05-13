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

import java.util.Date
import javax.inject.Inject

import org.apache.commons.collections.Closure
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import org.springframework.transaction.annotation.Transactional

import org.examproject.blog.dto.EntryDto
import org.examproject.blog.entity.Entry
import org.examproject.blog.util.EntryUtils
import org.examproject.blog.util.GeneralUtils
import org.examproject.blog.util.ParagraphUtils
import org.examproject.blog.util.TagUtils

import scala.collection.JavaConversions._

/**
 * @author hiroxpepe
 */
class SaveEntryClosure extends Closure {
    
    private val LOG: Logger = LoggerFactory.getLogger(
        classOf[SaveEntryClosure]
    )

    @Inject
    private val context: ApplicationContext = null
    
    @Inject
    private val entryUtils: EntryUtils = null
    
    @Inject
    private val paragraphUtils: ParagraphUtils = null
    
    @Inject
    private val tagUtils: TagUtils = null
    
    ///////////////////////////////////////////////////////////////////////////
    // public methods
    
    @Override
    def execute(o: Object ) = {
        LOG.debug("called.")
        val entryDto: EntryDto = o.asInstanceOf[EntryDto]
        try {
            save(entryDto)
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
    private def save(
        entryDto: EntryDto
    ) = {
        LOG.debug("called save.")
        try {
            // if dto is new one, create a new date and code.
            if (entryDto.getCode.equals("")) {
                LOG.debug("create the new entry.")
                entryDto.setCreated(new Date())
                entryDto.setCode(GeneralUtils.createCode())
            }
                        
            // get the entry.
            val entry: Entry = entryUtils.getEntry(entryDto)
            
            // map the dto value to the entity.
            entry.setAuthor(entryDto.getAuthor())
            entry.setParagraphSet(paragraphUtils.getParagraphSet(entryDto, entry))   
            entry.setTagItemSet(tagUtils.getTagItemSet(entryDto, entry))
            entry.setCreated(entryDto.getCreated())
            entry.setUpdated(entryDto.getCreated())
            entry.setCode(entryDto.getCode())

            // push the entity to repository.
            entryUtils.saveEntry(entry)
            
            LOG.debug("save a entry.")
            
            // if dto is new one, set the entity's id.
            if (entryDto.getId() == null) {
                entryDto.setId(entry.getId())
            }
            
        } catch {
            case e: Exception => {
                // TODO: LOG ERROR
                LOG.error(e.toString())
                throw new RuntimeException("failed save a entry.", e)
            }
        }
    }
    
}