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

package org.expt.blog.util;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import org.expt.blog.dto.EntryDto;
import org.expt.blog.entity.Category;
import org.expt.blog.repository.CategoryRepository;

/**
 * @author h.adachi
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class CategoryUtils {

    @NonNull
    private final ApplicationContext context;

    @NonNull
    private final CategoryRepository categoryRepository;

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
                categoryRepository.save(newCategory);
                log.debug("create the new category.");
                return newCategory;
            }
            return category;
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new RuntimeException("an error occurred.", e);
        }
    }
}
