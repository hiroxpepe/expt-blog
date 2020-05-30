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

import java.util.Date;
import java.util.Set;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import org.examproject.blog.dto.EntryDto;
import org.examproject.blog.entity.Entry;
import org.examproject.blog.entity.CategoryItem;
import org.examproject.blog.entity.Subject;
import org.examproject.blog.repository.CategoryItemRepository;
import org.examproject.blog.repository.SubjectRepository;

/**
 * @author h.adachi
 */
@Component
public class SubjectUtils {

    private Logger LOG = LoggerFactory.getLogger(SubjectUtils.class);

    @Inject
    private ApplicationContext context = null;

    @Inject
    private CategoryItemRepository categoryItemRepository = null;

    @Inject
    private SubjectRepository subjectRepository = null;

    @Inject
    private CategoryUtils categoryUtils = null;

    ///////////////////////////////////////////////////////////////////////////
    // public methods

    /**
     * get the subject.
     */
    public Subject getSubject(
        EntryDto entryDto
    ){
        try {
            if (entryDto.getSubject().equals("")) {
                return getDefaultSubject(entryDto);
            }
            Subject subject = subjectRepository.findByText(entryDto.getSubject());
            if (subject == null) {
                return getNewSubject(entryDto);
            }
            return subject;
        } catch (Exception e) {
            throw new RuntimeException("an error occurred.", e);
        }
    }

    /**
     * get the category string of this.
     */
    public String getCategoryString(
        Entry entry
    ){
        try {
            Subject subject = entry.getSubject();
            Set<CategoryItem> categoryItemSet = subject.getCategoryItemSet();
            // TODO: if composite..
            for (CategoryItem categoryItem : categoryItemSet) {
                return categoryItem.getCategory().getText();
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("an error occurred.", e);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // private methods

    /**
     * get the default subject.
     */
    private Subject getDefaultSubject(
        EntryDto entryDto
    ){
        try {
            Subject subject = subjectRepository.findByText(entryDto.getAuthor());
            if (subject == null) {
                Subject newSubject = context.getBean(Subject.class);
                 Set<CategoryItem> categoryItemSet = categoryUtils.getDefaultCategoryItemSet(newSubject);
                newSubject.setAuthor(entryDto.getAuthor());
                newSubject.setCreated(new Date());
                newSubject.setUpdated(new Date());
                newSubject.setText(entryDto.getAuthor());
                newSubject.setCategoryItemSet(categoryItemSet);
                subjectRepository.save(newSubject);
                LOG.debug("create the subject.");
                for (CategoryItem categoryItem : categoryItemSet) {
                    categoryItemRepository.save(categoryItem);
                }
                return newSubject;
            }
            return subject;
        } catch (Exception e) {
            throw new RuntimeException("an error occurred.", e);
        }
    }

    /**
     * get the new subject.
     */
    private Subject getNewSubject(
        EntryDto entryDto
    ){
        try {
            Subject subject = context.getBean(Subject.class);
            Set<CategoryItem> categoryItemSet = categoryUtils.getCategoryItemSet(entryDto, subject);
            subject.setAuthor(entryDto.getAuthor());
            subject.setCreated(new Date());
            subject.setUpdated(new Date());
            subject.setText(entryDto.getSubject());
            subject.setCategoryItemSet(categoryItemSet);
            subjectRepository.save(subject);
            LOG.debug("create the subject.");
            for (CategoryItem categoryItem : categoryItemSet) {
                categoryItemRepository.save(categoryItem);
            }
            return subject;
        } catch (Exception e) {
            throw new RuntimeException("an error occurred.", e);
        }
    }

}
