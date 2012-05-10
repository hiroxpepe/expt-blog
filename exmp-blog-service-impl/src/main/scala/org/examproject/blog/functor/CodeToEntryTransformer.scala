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
import java.util.List
import javax.inject.Inject

import org.apache.commons.collections.Transformer
import org.dozer.Mapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import org.springframework.transaction.annotation.Transactional

import org.examproject.blog.dto.EntryDto
import org.examproject.blog.entity.Entry
import org.examproject.blog.repository.EntryRepository

/**
 * @author hiroxpepe
 */
class CodeToEntryTransformer extends Transformer {

    private val LOG: Logger = LoggerFactory.getLogger(
        classOf[CodeToEntryTransformer]
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
        
        // if 'code' is offered, find the entity from repository, and mapping to dto.
        if (o != null) {
            // get the entity from repository.
            val code: String = o.toString()
            val entryList: List[Entry] = repository.findByCode(code)
            if (entryList.size == 0) {
                return null
            }
            
            // TODO: if the code is not unique
            val entry: Entry = entryList.get(0)
            
            // object mapping by dozer.
            val dto: EntryDto = context.getBean(classOf[EntryDto])
            mapper.map(entry, dto)
            
            // return a mapped dto.
            return dto
            
        } else {
            // return null..
            return null
        }
    }
    
}