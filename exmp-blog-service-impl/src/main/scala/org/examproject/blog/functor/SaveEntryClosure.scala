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
import java.util.List
import java.util.Set
import java.util.HashSet
import javax.inject.Inject

import org.apache.commons.collections.Closure
import org.dozer.Mapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import org.springframework.transaction.annotation.Transactional

import org.examproject.blog.dto.EntryDto
import org.examproject.blog.entity.Category
import org.examproject.blog.entity.CategoryItem
import org.examproject.blog.entity.Cluster
import org.examproject.blog.entity.Entry
import org.examproject.blog.entity.Group
import org.examproject.blog.entity.Interest
import org.examproject.blog.entity.Paragraph
import org.examproject.blog.entity.Subject
import org.examproject.blog.entity.Tag
import org.examproject.blog.entity.TagItem
import org.examproject.blog.entity.User
import org.examproject.blog.repository.CategoryRepository
import org.examproject.blog.repository.CategoryItemRepository
import org.examproject.blog.repository.ClusterRepository
import org.examproject.blog.repository.EntryRepository
import org.examproject.blog.repository.GroupRepository
import org.examproject.blog.repository.InterestRepository
import org.examproject.blog.repository.ParagraphRepository
import org.examproject.blog.repository.SubjectRepository
import org.examproject.blog.repository.TagRepository
import org.examproject.blog.repository.TagItemRepository
import org.examproject.blog.repository.UserRepository
import org.examproject.blog.util.EntryUtils

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
    private val mapper: Mapper = null
    
    @Inject
    private val entryRepository: EntryRepository = null
    
    @Inject
    private val categoryRepository: CategoryRepository = null
    
    @Inject
    private val categoryItemRepository: CategoryItemRepository = null
    
    @Inject
    private val clusterItemRepository: ClusterRepository = null
    
    @Inject
    private val groupRepository: GroupRepository = null
    
    @Inject
    private val interestRepository: InterestRepository = null
    
    @Inject
    private val paragraphRepository: ParagraphRepository = null
    
    @Inject
    private val subjectRepository: SubjectRepository = null
    
    @Inject
    private val tagRepository: TagRepository = null
    
    @Inject
    private val tagItemRepository: TagItemRepository = null
    
    @Inject
    private val userRepository: UserRepository = null
    
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
                entryDto.setCode(EntryUtils.createCode())
            }
            
            // get the entry.
            val entry: Entry = getEntry(entryDto)
            entry.setAuthor(entryDto.getAuthor())
            entry.setParagraphSet(getParagraphSet(entryDto, entry))
            entry.setTagItemSet(getDefaultTagItemSet(entry))
            entry.setCreated(entryDto.getCreated())
            entry.setUpdated(entryDto.getCreated())
            entry.setCode(entryDto.getCode())

            // push the entity to repository.
            saveEntity(entry)
            
            LOG.debug("save a entry.")
            
            // if dto is new one, set the entity's id.
            if (entryDto.getId() == null) {
                entryDto.setId(entry.getId())
            }
            
        } catch {
            case e: Exception => {
                // TODO: LOG ERROR
                throw new RuntimeException("failed save a entry.", e)
            }
        }
    }
    
    private def saveEntity(
        entry: Entry
    ) {
        entryRepository.save(entry)
        
        val paragraphSet: Set[Paragraph] = entry.getParagraphSet()
        for (paragraph: Paragraph <- paragraphSet) {
            paragraphRepository.save(paragraph)
        }
        
        val tagItemSet: Set[TagItem] = entry.getTagItemSet()
        for (tagItem: TagItem <- tagItemSet) {
            tagItemRepository.save(tagItem)
        }
    }
    
    private def getParagraphSet(
        entryDto: EntryDto,
        entry: Entry
    )
    : Set[Paragraph] = {
        val titleParagraph: Paragraph = getTitle(entryDto, entry)
        val contentParagraph: Paragraph = getContent(entryDto, entry)
        val paragraphSet: Set[Paragraph] = new HashSet[Paragraph]
        paragraphSet.add(titleParagraph)
        paragraphSet.add(contentParagraph)
        return paragraphSet
    }

    private def getTitle(
        entryDto: EntryDto,
        entry: Entry
    )
    : Paragraph = {
        val titleParagraph: Paragraph = context.getBean(classOf[Paragraph])
        titleParagraph.setContent(entryDto.getTitle())
        titleParagraph.setKind("title")
        titleParagraph.setCreated(new Date())
        titleParagraph.setUpdated(new Date())
        titleParagraph.setEntry(entry)
        return titleParagraph
    }
    
    private def getContent(
        entryDto: EntryDto,
        entry: Entry
    )
    : Paragraph = {
        val contentParagraph: Paragraph = context.getBean(classOf[Paragraph])
        contentParagraph.setContent(entryDto.getContent())
        contentParagraph.setKind("content")
        contentParagraph.setCreated(new Date())
        contentParagraph.setUpdated(new Date())
        contentParagraph.setEntry(entry)
        return contentParagraph
    }
    
    private def getEntry(
        entryDto: EntryDto
    )
    : Entry = {
        if (entryDto.getId() == null) {
            val entry = context.getBean(classOf[Entry])
            entry.setUser(getUser(entryDto))
            //entry.setSubject(getSubject(entryDto))
            entry.setSubject(getDefaultSubject(entryDto))
            LOG.debug("create entry.")
            return entry
        } else {
            val entry = entryRepository.findOne(entryDto.getId()).asInstanceOf[Entry]
            LOG.debug("update entry.")
            return entry
        }
    }
    
    ///////////////////////////////////////////////////////////////////////////
    // user
    
    private def getUser(
        entryDto: EntryDto
    )
    : User = {
        val user: User = userRepository.findByUsername(entryDto.getUsername())
        if (user == null) {
            val newUser = context.getBean(classOf[User])
            newUser.setUsername(entryDto.getUsername())
            newUser.setPassword(entryDto.getPassword())
            userRepository.save(newUser)
            LOG.debug("create the new user.")
            val group: Group = context.getBean(classOf[Group])
            group.setUser(newUser)
            group.setName("own")
            groupRepository.save(group)
            
            //getDefaultInterestSet()
            return newUser
        }
        return user
    }
    
    ///////////////////////////////////////////////////////////////////////////
    // subject
    
    private def getSubject(
        entryDto: EntryDto
    )
    : Subject = {
        return subjectRepository.findByText(entryDto.getSubject())
    }
    
    private def getDefaultSubject(
        entryDto: EntryDto
    )
    : Subject = {
        val subject: Subject = subjectRepository.findByText(entryDto.getAuthor())
        if (subject == null) {
            val newSubject: Subject = context.getBean(classOf[Subject])
            val categoryItemSet: Set[CategoryItem] = getDefaultCategoryItemSet(newSubject)
            newSubject.setAuthor(entryDto.getAuthor())
            newSubject.setCreated(new Date())
            newSubject.setUpdated(new Date())
            newSubject.setText(entryDto.getAuthor())
            newSubject.setCategoryItemSet(categoryItemSet)
            subjectRepository.save(newSubject)
            for (categoryItem: CategoryItem <- categoryItemSet) {
                categoryItemRepository.save(categoryItem)
            }
            return newSubject
        }
        return subject
    }
    
    ///////////////////////////////////////////////////////////////////////////
    // category
    
    private def getDefaultCategory()
    : Category = {
        return categoryRepository.findByText("General")
    }
    
    private def getDefaultCategoryItemSet(
        subject: Subject
    )
    : Set[CategoryItem] = {
        val category: Category = getDefaultCategory()
        val categoryItem: CategoryItem = context.getBean(classOf[CategoryItem])
        categoryItem.setSubject(subject)
        categoryItem.setCategory(category)
        val categoryItemSet: Set[CategoryItem] = new HashSet[CategoryItem]
        categoryItemSet.add(categoryItem)
        return categoryItemSet
    }
    
    ///////////////////////////////////////////////////////////////////////////
    // interest
    
    private def getDefaultInterest(
        entryDto: EntryDto
    )
    : Interest = {
        val user: User = getUser(entryDto)
        val interest: Interest = interestRepository.findByUserAndName(user, entryDto.getUsername())
        if (interest == null) {
           val newInterest: Interest = context.getBean(classOf[Interest])
            newInterest.setUser(user)
            newInterest.setName(entryDto.getUsername())
            interestRepository.save(interest)
            return newInterest
        }
        return interest
    }
    
    private def getDefaultInterestSet(
        entryDto: EntryDto
    )
    : Set[Interest] = {
        val interest: Interest = getDefaultInterest(entryDto)
        val interestSet: Set[Interest] = new HashSet[Interest]
        interestSet.add(interest)
        return interestSet
    }
    
    ///////////////////////////////////////////////////////////////////////////
    // tag
    
    private def getDefaultTag()
    : Tag = {
        return tagRepository.findByText("default")
    }
    
    private def getDefaultTagItemSet(
        entry: Entry
    )
    : Set[TagItem] = {
        val tag: Tag = getDefaultTag()
        val tagItem: TagItem = context.getBean(classOf[TagItem])
        tagItem.setEntry(entry)
        tagItem.setTag(tag)
        val tagItemSet: Set[TagItem] = new HashSet[TagItem]
        tagItemSet.add(tagItem)
        return tagItemSet
    }
    
    private def getTagItemSet(
        entry: Entry
    )
    : Set[TagItem] = {
        val tagItemList: List[TagItem] = tagItemRepository.findByEntry(entry)
        val tagItemSet: Set[TagItem] = new HashSet[TagItem]()
        for (tagItem: TagItem <- tagItemList) {
            tagItemSet.add(tagItem)
        }
        return tagItemSet
    }
    
}
