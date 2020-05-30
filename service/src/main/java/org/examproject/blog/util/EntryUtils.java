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
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import org.examproject.blog.dto.EntryDto;
import org.examproject.blog.entity.Entry;
import org.examproject.blog.entity.Paragraph;
import org.examproject.blog.entity.TagItem;
import org.examproject.blog.repository.EntryRepository;
import org.examproject.blog.repository.ParagraphRepository;
import org.examproject.blog.repository.TagItemRepository;

/**
 * @author h.adachi
 */
@Component
public class EntryUtils {

    private final Logger LOG = LoggerFactory.getLogger(EntryUtils.class);

    @Inject
    private final ApplicationContext context = null;

    @Inject
    private final EntryRepository entryRepository = null;

    @Inject
    private final ParagraphRepository paragraphRepository = null;

    @Inject
    private final TagItemRepository tagItemRepository = null;

    @Inject
    private final ParagraphUtils paragraphUtils = null;

    @Inject
    private final SubjectUtils subjectUtils = null;

    @Inject
    private final TagUtils tagUtils = null;

    @Inject
    private final UserUtils userUtils = null;

    ///////////////////////////////////////////////////////////////////////////
    // public methods

    ///////////////////////////////////////////////////////////////////////////
    /**
     * get the entry dto.
     */
    public Entry getEntry(
        EntryDto entryDto
    ){
        try {
            if (entryDto.getId() == null) {
                Entry entry = context.getBean(Entry.class);
                entry.setUser(userUtils.getUser(entryDto));
                LOG.debug("create entry.");
                return entry;
            } else {
                //val entry = entryRepository.findOne(
                //    entryDto.getId()
                //).asInstanceOf[Entry]
                Entry entry = entryRepository.getOne(entryDto.getId());
                LOG.debug("update entry.");
                return entry;
            }
        } catch (Exception e) {
            throw new RuntimeException("an error occurred.", e);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    /**
     * get the entry entity.
     */
    public void saveEntry(
        Entry entry
    ) {
        try {
            if (entry.getId() == null) {
                entryRepository.save(entry);
                Set<Paragraph> paragraphSet = entry.getParagraphSet();
                for (Paragraph paragraph : paragraphSet) {
                    paragraphRepository.save(paragraph);
                }
                Set<TagItem> tagItemSet = entry.getTagItemSet();
                for (TagItem tagItem : tagItemSet) {
                    tagItemRepository.save(tagItem);
                }
            }
            else if (entry.getId() != null) {
                Set<Paragraph> paragraphSet = entry.getParagraphSet();
                for (Paragraph paragraph : paragraphSet) {
                    paragraphRepository.save(paragraph);
                }
                Set<TagItem> tagItemSet = entry.getTagItemSet();
                for (TagItem tagItem : tagItemSet) {
                    tagItemRepository.save(tagItem);
                }
                entryRepository.save(entry);
            }
        } catch (Exception e) {
            throw new RuntimeException("an error occurred.", e);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    /**
     * map the entry entity to the entry dto.
     */
    public EntryDto mapEntry(
        Entry entry,
        EntryDto entryDto
    ){
        try {
            // map the entity to the dto.
            entryDto.setId(entry.getId());
            entryDto.setUsername(entry.getUser().getUsername());
            entryDto.setPassword(entry.getUser().getPassword());
            entryDto.setEmail(entry.getUser().getEmail());
            entryDto.setAuthor(entry.getAuthor());
            entryDto.setTitle(paragraphUtils.getTitleString(entry));
            entryDto.setContent(paragraphUtils.getContentString(entry));
            entryDto.setSubject(entry.getSubject().getText());
            entryDto.setCategory(subjectUtils.getCategoryString(entry));
            entryDto.setTags(tagUtils.getTagItemString(entry));
            entryDto.setCreated(entry.getCreated());
            entryDto.setCode(entry.getCode());

            return entryDto;
        } catch (Exception e) {
            throw new RuntimeException("an error occurred.", e);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    /**
     * map the entry dto to the entry entity.
     */
    public Entry mapEntry(
        EntryDto entryDto,
        Entry entry
    ){
        try {
            // map the dto value to the entity.
            entry.setAuthor(entryDto.getAuthor());
            entry.setParagraphSet(paragraphUtils.getParagraphSet(entryDto, entry));
            entry.setTagItemSet(tagUtils.getTagItemSet(entryDto, entry));
            entry.setCreated(entryDto.getCreated());
            entry.setUpdated(entryDto.getCreated());
            entry.setCode(entryDto.getCode());
            entry.setSubject(subjectUtils.getSubject(entryDto));
            return entry;
        } catch (Exception e) {
            throw new RuntimeException("an error occurred.", e);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    /**
     * delete the entry entity.
     */
    public void deleteEntry(
        EntryDto entryDto
    ) {
        try {
            // to search the repository for delete.
            //val entry: Entry = entryRepository.findOne(
            //    entryDto.getId();
            //).asInstanceOf[Entry]
            Entry entry = entryRepository.getOne(entryDto.getId());

            // delete the entry's paragraphs.
            Set<Paragraph> paragraphSet = entry.getParagraphSet();
            for (Paragraph paragraph : paragraphSet) {
                //paragraphRepository.delete(paragraph.getId());
                paragraphRepository.delete(paragraph);
            }

            // delete the entry's tagitems.
            Set<TagItem> tagItemSet = entry.getTagItemSet();
            for (TagItem tagItem : tagItemSet) {
                //tagItemRepository.delete(tagItem.getId());
                tagItemRepository.delete(tagItem);
            }

            // delete the entry.
            //entryRepository.delete(entry.getId());
            entryRepository.delete(entry);
        } catch (Exception e) {
            throw new RuntimeException("an error occurred.", e);
        }
    }

}
