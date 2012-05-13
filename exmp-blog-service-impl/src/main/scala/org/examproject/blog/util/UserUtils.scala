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

import javax.inject.Inject

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

import org.examproject.blog.dto.EntryDto
import org.examproject.blog.entity.Group
import org.examproject.blog.entity.User
import org.examproject.blog.repository.GroupRepository
import org.examproject.blog.repository.UserRepository

import scala.collection.JavaConversions._

/**
 * @author hiroxpepe
 */
@Component
class UserUtils {

    private val LOG: Logger = LoggerFactory.getLogger(
        classOf[UserUtils]
    )

    @Inject
    private val context: ApplicationContext = null
  
    @Inject
    private val groupRepository: GroupRepository = null
    
    @Inject
    private val userRepository: UserRepository = null
    
    ///////////////////////////////////////////////////////////////////////////
    // public methods
    
    def getUser(
        entryDto: EntryDto
    )
    : User = {
        try {
            val user: User = userRepository.findByUsername(entryDto.getUsername())
            if (user == null) {
                val newUser = context.getBean(classOf[User])
                newUser.setUsername(entryDto.getUsername())
                newUser.setPassword(entryDto.getPassword())
                newUser.setEmail(entryDto.getEmail())
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
        } catch {
            case e: Exception => {
                throw new RuntimeException("an error occurred.", e)
            }
        }
    }
    
}
