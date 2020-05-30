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

package org.examproject.blog.util;

import java.util.Set;
import java.util.HashSet;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import org.examproject.blog.dto.EntryDto;
import org.examproject.blog.entity.Category;
import org.examproject.blog.entity.CategoryItem;
import org.examproject.blog.entity.Subject;
import org.examproject.blog.repository.CategoryRepository;
import org.examproject.blog.repository.CategoryItemRepository;


/**
 * @author hiroxpepe
 */
@Component
public class CategoryUtils {

    private final Logger LOG = LoggerFactory.getLogger(CategoryUtils.class);

    @Inject
    private final ApplicationContext context = null;

    @Inject
    private final CategoryRepository categoryRepository = null;

    @Inject
    private final CategoryItemRepository categoryItemRepository = null;

    ///////////////////////////////////////////////////////////////////////////
    // public methods

    ///////////////////////////////////////////////////////////////////////////
    /**
     * get the default categoryitem set.
     */
    public Set<CategoryItem> getDefaultCategoryItemSet(
        Subject subject
    ){
        try {
            Category category = getDefaultCategory();
            CategoryItem categoryItem = context.getBean(CategoryItem.class);
            categoryItem.setSubject(subject);
            categoryItem.setCategory(category);
            Set<CategoryItem> categoryItemSet = new HashSet<>();
            categoryItemSet.add(categoryItem);
            return categoryItemSet;
        } catch (Exception e) {
            throw new RuntimeException("an error occurred.", e);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    /**
     * get the categoryitem set.
     */
    public Set<CategoryItem> getCategoryItemSet(
        EntryDto entryDto,
        Subject subject
    ){
        try {
            Category category = getCategory(entryDto);
            CategoryItem categoryItem = context.getBean(CategoryItem.class);
            categoryItem.setSubject(subject);
            categoryItem.setCategory(category);
            Set<CategoryItem> categoryItemSet = new HashSet<>();
            categoryItemSet.add(categoryItem);
            return categoryItemSet;
        } catch (Exception e) {
            throw new RuntimeException("an error occurred.", e);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // private methods

    ///////////////////////////////////////////////////////////////////////////
    /**
     * get the default category.
     */
    private Category getDefaultCategory() {
        try {
            return categoryRepository.findByText("General");
        } catch (Exception e) {
            throw new RuntimeException("an error occurred.", e);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    /**
     * get the category string.
     */
    private Category getCategory(
        EntryDto entryDto
    ) {
        try {
            Category category = categoryRepository.findByText(entryDto.getCategory());
            if (category == null) {
                return getNewCategory(entryDto);
            }
            return category;
        } catch (Exception e) {
            throw new RuntimeException("an error occurred.", e);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    /**
     * get the new category.
     */
    private Category getNewCategory(
        EntryDto entryDto
    ) {
        try {
            // TODO: the category cannot create by user.
            return null;
        } catch (Exception e) {
            throw new RuntimeException("an error occurred.", e);
        }
    }

}
