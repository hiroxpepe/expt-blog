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
import javax.inject.Inject

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

import org.examproject.blog.dto.EntryDto
import org.examproject.blog.entity.Entry
import org.examproject.blog.entity.CategoryItem
import org.examproject.blog.entity.Subject
import org.examproject.blog.repository.CategoryItemRepository
import org.examproject.blog.repository.SubjectRepository

import scala.collection.JavaConversions._

/**
 * @author hiroxpepe
 */
@Component
class SubjectUtils {
    
    private val LOG: Logger = LoggerFactory.getLogger(
        classOf[SubjectUtils]
    )

    @Inject
    private val context: ApplicationContext = null
    
    @Inject
    private val categoryItemRepository: CategoryItemRepository = null
    
    @Inject
    private val subjectRepository: SubjectRepository = null
    
    @Inject
    private val categoryUtils: CategoryUtils = null
    
    ///////////////////////////////////////////////////////////////////////////
    // public methods
    
    ///////////////////////////////////////////////////////////////////////////
    /**
     * get the subject.
     */
    def getSubject(
        entryDto: EntryDto
    )
    : Subject = {
        try {
            if (entryDto.getSubject().equals("")) {
                return getDefaultSubject(entryDto)
            }
            val subject: Subject = subjectRepository.findByText(entryDto.getSubject())
            if (subject == null) {
                return getNewSubject(entryDto)
            }
            return subject
        } catch {
            case e: Exception => {
                throw new RuntimeException("an error occurred.", e)
            }
        }
    }
    
    ///////////////////////////////////////////////////////////////////////////
    /**
     * get the category string of this.
     */
    def getCategoryString(
        entry: Entry
    )
    : String = {
        try {
            val subject: Subject = entry.getSubject()
            val categoryItemSet: Set[CategoryItem] = subject.getCategoryItemSet
            // TODO: if composite..
            for (categoryItem: CategoryItem <- categoryItemSet) {
                return categoryItem.getCategory.getText()
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
     * get the default subject.
     */
    private def getDefaultSubject(
        entryDto: EntryDto
    )
    : Subject = {
        try {
            val subject: Subject = subjectRepository.findByText(entryDto.getAuthor())
            if (subject == null) {
                val newSubject: Subject = context.getBean(classOf[Subject])
                val categoryItemSet: Set[CategoryItem] = categoryUtils.getDefaultCategoryItemSet(newSubject)
                newSubject.setAuthor(entryDto.getAuthor())
                newSubject.setCreated(new Date())
                newSubject.setUpdated(new Date())
                newSubject.setText(entryDto.getAuthor())
                newSubject.setCategoryItemSet(categoryItemSet)
                subjectRepository.save(newSubject)
                LOG.debug("create the subject.")
                for (categoryItem: CategoryItem <- categoryItemSet) {
                    categoryItemRepository.save(categoryItem)
                }
                return newSubject
            }
            return subject
        } catch {
            case e: Exception => {
                throw new RuntimeException("an error occurred.", e)
            }
        }
    }
    
    ///////////////////////////////////////////////////////////////////////////
    /**
     * get the new subject.
     */
    private def getNewSubject(
        entryDto: EntryDto
    )
    : Subject ={
        try {
            val subject: Subject = context.getBean(classOf[Subject])
            val categoryItemSet: Set[CategoryItem] = categoryUtils.getCategoryItemSet(entryDto, subject)
            subject.setAuthor(entryDto.getAuthor())
            subject.setCreated(new Date())
            subject.setUpdated(new Date())
            subject.setText(entryDto.getSubject())
            subject.setCategoryItemSet(categoryItemSet)
            subjectRepository.save(subject)
            LOG.debug("create the subject.")
            for (categoryItem: CategoryItem <- categoryItemSet) {
                categoryItemRepository.save(categoryItem)
            }
            return subject
        } catch {
            case e: Exception => {
                throw new RuntimeException("an error occurred.", e)
            }
        }
    }
}