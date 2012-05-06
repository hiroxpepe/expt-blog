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
import org.examproject.blog.entity.Subject
import org.examproject.blog.entity.Entry
import org.examproject.blog.entity.User
import org.examproject.blog.repository.EntryRepository
import org.examproject.blog.repository.SubjectRepository
import org.examproject.blog.repository.UserRepository
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
    private val entryRepository: EntryRepository = null
    
    @Inject
    private val subjectRepository: SubjectRepository = null
    
    @Inject
    private val userRepository: UserRepository = null
    
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
            LOG.debug("create the new entry.")
            entryDto.setCreated(new Date())
            entryDto.setCode(EntryUtils.createCode())
        }
        
        // map the object.
        var subject: Subject = context.getBean(classOf[Subject])
        subject.setCreated(new Date())
        subject.setAuthor(entryDto.getAuthor())
        subject.setText("hoge" + subject.getCreated.toString())
        subject = subjectRepository.save(subject)
        
        var user: User = userRepository.findByUsername(entryDto.getUsername())
        if (user == null) {
            user = context.getBean(classOf[User])
            user.setUsername(entryDto.getUsername())
            user.setPassword(entryDto.getPassword())
            user = userRepository.save(user)
        }
        
        val entry: Entry = context.getBean(classOf[Entry])
        entry.setId(entryDto.getId)
        entry.setAuthor(entryDto.getAuthor())
        entry.setTitle(entryDto.getTitle())
        entry.setContent(entryDto.getContent())
        entry.setCreated(entryDto.getCreated())
        entry.setCode(entryDto.getCode())
        entry.setUser(user)
        entry.setSubject(subject)

        // push the entity to repository.
        try {
            entryRepository.save(entry)
            LOG.debug("save a entry.")
        } catch {
            case e: Exception => {
                throw new RuntimeException("failed save a entry.", e)
            }
        }
        
        // if dto is new one, set the entity's id.
        if (entryDto.getId() == null) {
            entryDto.setId(entry.getId())
        }
    }

}
