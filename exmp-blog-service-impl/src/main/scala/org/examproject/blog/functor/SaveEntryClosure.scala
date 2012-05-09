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
import java.util.Set
import java.util.HashSet
import javax.inject.Inject

import org.apache.commons.collections.Closure
import org.dozer.Mapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext

import org.examproject.blog.dto.EntryDto
import org.examproject.blog.entity.Entry
import org.examproject.blog.entity.Paragraph
import org.examproject.blog.entity.Subject
import org.examproject.blog.entity.User
import org.examproject.blog.repository.EntryRepository
import org.examproject.blog.repository.ParagraphRepository
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
    private val paragraphRepository: ParagraphRepository = null
    
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
        try {
            // if dto is new one, create a new date and code.
            if (entryDto.getCreated() == null) {
                LOG.debug("create the new entry.")
                entryDto.setCreated(new Date())
                entryDto.setCode(EntryUtils.createCode())
            }

            // map the object.
            val subject: Subject = context.getBean(classOf[Subject])
            subject.setCreated(new Date())
            subject.setUpdated(new Date())
            subject.setAuthor(entryDto.getAuthor())
            subject.setText("hoge" + subject.getCreated.toString())
            subjectRepository.save(subject)

            var user: User = userRepository.findByUsername(entryDto.getUsername())
            if (user == null) {
                user = context.getBean(classOf[User])
                user.setUsername(entryDto.getUsername())
                user.setPassword(entryDto.getPassword())
                user = userRepository.save(user)
            }

            val entry: Entry = context.getBean(classOf[Entry])
            
            val titleParagraph: Paragraph = context.getBean(classOf[Paragraph])
            titleParagraph.setContent(entryDto.getTitle())
            titleParagraph.setKind("title")
            titleParagraph.setCreated(new Date())
            titleParagraph.setUpdated(new Date())
            titleParagraph.setEntry(entry)

            val contentParagraph: Paragraph = context.getBean(classOf[Paragraph])
            contentParagraph.setContent(entryDto.getContent())
            contentParagraph.setKind("content")
            contentParagraph.setCreated(new Date())
            contentParagraph.setUpdated(new Date())
            contentParagraph.setEntry(entry)

            val paragraphSet: Set[Paragraph] = new HashSet[Paragraph]
            paragraphSet.add(titleParagraph)
            paragraphSet.add(contentParagraph)

            entry.setId(entryDto.getId)
            entry.setAuthor(entryDto.getAuthor())
            entry.setParagraphSet(paragraphSet)
            entry.setCreated(entryDto.getCreated())
            entry.setUpdated(entryDto.getCreated())
            entry.setCode(entryDto.getCode())
            entry.setUser(user)
            entry.setSubject(subject)

            // push the entity to repository.
            entryRepository.save(entry)
            paragraphRepository.save(titleParagraph)
            paragraphRepository.save(contentParagraph)
            
            LOG.debug("save a entry.")
            
            // if dto is new one, set the entity's id.
            if (entryDto.getId() == null) {
                entryDto.setId(entry.getId())
            }
            
        } catch {
            case e: Exception => {
                throw new RuntimeException("failed save a entry.", e)
            }
        }
    }

}
