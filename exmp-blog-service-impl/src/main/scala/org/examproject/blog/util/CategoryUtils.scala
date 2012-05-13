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

import org.examproject.blog.entity.Category
import org.examproject.blog.entity.CategoryItem
import org.examproject.blog.entity.Subject
import org.examproject.blog.repository.CategoryRepository
import org.examproject.blog.repository.CategoryItemRepository

import scala.collection.JavaConversions._

/**
 * @author hiroxpepe
 */
@Component
class CategoryUtils {
    
    private val LOG: Logger = LoggerFactory.getLogger(
        classOf[CategoryUtils]
    )

    @Inject
    private val context: ApplicationContext = null
    
    @Inject
    private val categoryRepository: CategoryRepository = null
    
    @Inject
    private val categoryItemRepository: CategoryItemRepository = null
    
    ///////////////////////////////////////////////////////////////////////////
    // public methods
    
    def getDefaultCategory()
    : Category = {
        try {
            return categoryRepository.findByText("General")
        } catch {
            case e: Exception => {
                throw new RuntimeException("an error occurred.", e)
            }
        }
    }
    
    def getDefaultCategoryItemSet(
        subject: Subject
    )
    : Set[CategoryItem] = {
        try {
            val category: Category = getDefaultCategory()
            val categoryItem: CategoryItem = context.getBean(classOf[CategoryItem])
            categoryItem.setSubject(subject)
            categoryItem.setCategory(category)
            val categoryItemSet: Set[CategoryItem] = new HashSet[CategoryItem]
            categoryItemSet.add(categoryItem)
            return categoryItemSet
        } catch {
            case e: Exception => {
                throw new RuntimeException("an error occurred.", e)
            }
        }
    }
    
}
