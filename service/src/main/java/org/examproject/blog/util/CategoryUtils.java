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

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import org.examproject.blog.dto.EntryDto;
import org.examproject.blog.entity.Category;
import org.examproject.blog.repository.CategoryRepository;

/**
 * @author h.adachi
 */
@Component
public class CategoryUtils {

    private Logger LOG = LoggerFactory.getLogger(CategoryUtils.class);

    @Inject
    private ApplicationContext context = null;

    @Inject
    private CategoryRepository categoryRepository = null;

    ///////////////////////////////////////////////////////////////////////////
    // public methods

    public Category getCategory(
        EntryDto entryDto
    ){
        try {
            Category category = categoryRepository.findByText(entryDto.getCategory());
            if (category == null) {
                Category newCategory = context.getBean(Category.class);
                newCategory.setText(entryDto.getCategory());
                // newCategory.setPassword(entryDto.getPassword());
                // newCategory.setEmail(entryDto.getEmail());
                categoryRepository.save(newCategory);
                LOG.debug("create the new category.");
                return newCategory;
            }
            return category;
        } catch (Exception e) {
            throw new RuntimeException("an error occurred.", e);
        }
    }

}