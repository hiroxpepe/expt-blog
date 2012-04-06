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
import org.dozer.Mapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext

import org.examproject.blog.dto.EntryDto
import org.examproject.blog.entity.Entry
import org.examproject.blog.repository.EntryRepository
import org.examproject.blog.util.EntryUtils

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
    private val mapper: Mapper = null
    
    @Inject
    private val repository: EntryRepository = null
    
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
    
    private def save(entryDto: EntryDto) = {
        LOG.debug("called save.")

        // if dto is new one, create a new date and code.
        if (entryDto.getCreated() == null) {
            entryDto.setCreated(new Date())
            entryDto.setCode(EntryUtils.createCode())
            LOG.debug("set entryDto new Date.")
        }
        
        // object mapping by dozer.
        val entry: Entry = context.getBean(classOf[Entry])
        mapper.map(entryDto, entry)

        // push the entity to repository.
        try {
            repository.save(entry)
            LOG.debug("save a entry.")
        } catch {
            case e: Exception => {
                throw new RuntimeException("failed save a entry.", e)
            }
        }
        
        // if dto is new one, set the entity's ID.
        if (entryDto.getId() == null) {
            entryDto.setId(entry.getId())
        }
    }

}
