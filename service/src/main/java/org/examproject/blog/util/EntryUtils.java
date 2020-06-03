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

import java.util.List;
import java.util.Optional;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import org.examproject.blog.dto.EntryDto;
import org.examproject.blog.entity.Entry;
import org.examproject.blog.entity.EntryTag;
import org.examproject.blog.repository.EntryRepository;
import org.examproject.blog.repository.EntryTagRepository;

/**
 * @author h.adachi
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class EntryUtils {

    @NonNull
    private final ApplicationContext context;

    @NonNull
    private final EntryRepository entryRepository;

    @NonNull
    private final CategoryUtils categoryUtils;

    @NonNull
    private final TagUtils tagUtils;

    @NonNull
    private final UserUtils userUtils;

    @NonNull
    private final EntryTagRepository entryTagRepository;

    ///////////////////////////////////////////////////////////////////////////
    // public methods

    /**
     * get the entry dto.
     */
    public Entry getEntryWithId(
        EntryDto entryDto
    ){
        try {
            if (entryDto.getId() == null) {
                Entry entry = context.getBean(Entry.class);
                entryRepository.save(entry); // save it once to get the ld.
                entryDto.setId(entry.getId());
                log.info("new entry.id: " + entry.getId());
                log.debug("to create entry.");
                return entry;
            } else {
                Optional<Entry> optional = entryRepository.findById(entryDto.getId());
                Entry entry = optional.get();
                log.info("entry.id: " + entry.getId());
                log.debug("to update entry.");
                return entry;
            }
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new RuntimeException("an error occurred.", e);
        }
    }

    /**
     * save the entry entity.
     */
    public void saveEntry(
        Entry entry
    ) {
        try {
            // save the entry.
            entryRepository.save(entry);
            log.debug("save entry.");
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new RuntimeException("an error occurred.", e);
        }
    }

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
            entryDto.setTitle(entry.getTitle());
            entryDto.setContent(entry.getContent());
            entryDto.setCode(entry.getCode());
            entryDto.setCreated(entry.getCreated());
            entryDto.setCategory(entry.getCategory().getText());
            entryDto.setTags(tagUtils.getTagItemString(entry));
            entryDto.setUsername(entry.getUser().getUsername());
            entryDto.setPassword(entry.getUser().getPassword());
            entryDto.setEmail(entry.getUser().getEmail());
            return entryDto;
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new RuntimeException("an error occurred.", e);
        }
    }

    /**
     * map the entry dto to the entry entity.
     */
    public Entry mapEntry(
        EntryDto entryDto,
        Entry entry
    ){
        try {
            // map the dto value to the entity.
            entry.setTitle(entryDto.getTitle());
            entry.setContent(entryDto.getContent());
            entry.setCode(entryDto.getCode());
            entry.setCreated(entryDto.getCreated());
            entry.setUpdated(entryDto.getCreated());
            entry.setUser(userUtils.getUser(entryDto));
            entry.setCategory(categoryUtils.getCategory(entryDto));
            tagUtils.getEntryTagSet(entry, entryDto.getTags()); // FIXME: get the entryTag set from enrty itself!
            return entry;
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new RuntimeException("an error occurred.", e);
        }
    }

    /**
     * delete the entry entity.
     */
    public void deleteEntry(
        EntryDto entryDto
    ) {
        try {
            Entry entry = entryRepository.getOne(entryDto.getId()); // getOne() is lazily fetched.
            // delete the entryTag.
            List<EntryTag> entryTagList = entryTagRepository.findByEntry(entry);
            for (EntryTag entryTag : entryTagList) {
                entryTagRepository.delete(entryTag);
            }
            // delete the entry.
            entryRepository.delete(entry);
            log.debug("delete entry.");
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new RuntimeException("an error occurred.", e);
        }
    }

}
