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
import org.examproject.blog.entity.Interest
import org.examproject.blog.entity.User
import org.examproject.blog.repository.EntryRepository
import org.examproject.blog.repository.InterestRepository

import scala.collection.JavaConversions._

/**
 * @author hiroxpepe
 */
@Component
class InteresUtils {
    
    private val LOG: Logger = LoggerFactory.getLogger(
        classOf[InteresUtils]
    )

    @Inject
    private val context: ApplicationContext = null
    
    @Inject
    private val entryRepository: EntryRepository = null

    @Inject
    private val interestRepository: InterestRepository = null
    
    @Inject
    private val categoryUtils: CategoryUtils = null
    
    @Inject
    private val tagUtils: TagUtils = null
    
    @Inject
    private val userUtils: UserUtils = null
    
    ///////////////////////////////////////////////////////////////////////////
    // public methods
    
    def getDefaultInterest(
        entryDto: EntryDto
    )
    : Interest = {
        try {
            val user: User = userUtils.getUser(entryDto)
            val interest: Interest = interestRepository.findByUserAndName(user, entryDto.getUsername())
            if (interest == null) {
            val newInterest: Interest = context.getBean(classOf[Interest])
                newInterest.setUser(user)
                newInterest.setName(entryDto.getUsername())
                interestRepository.save(interest)
                return newInterest
            }
            return interest
        } catch {
            case e: Exception => {
                throw new RuntimeException("an error occurred.", e)
            }
        }
    }
    
    def getDefaultInterestSet(
        entryDto: EntryDto
    )
    : Set[Interest] = {
        try {
            val interest: Interest = getDefaultInterest(entryDto)
            val interestSet: Set[Interest] = new HashSet[Interest]
            interestSet.add(interest)
            return interestSet
        } catch {
            case e: Exception => {
                throw new RuntimeException("an error occurred.", e)
            }
        }
    }

}
