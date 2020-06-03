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

package org.examproject.blog.functor;

import java.util.ArrayList;
import java.util.List;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.Factory;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import org.examproject.blog.dto.EntryDto;
import org.examproject.blog.entity.Entry;
import org.examproject.blog.repository.EntryRepository;
import org.examproject.blog.util.EntryUtils;

/**
 * @author h.adachi
 */
@Slf4j
@RequiredArgsConstructor
public class AllEntryFactory implements Factory {

    @NonNull
    private final ApplicationContext context;

    @NonNull
    private final EntryRepository entryRepository;

    @NonNull
    private final EntryUtils entryUtils;

    ///////////////////////////////////////////////////////////////////////////
    // public methods

    @Override
    public Object create() {
        log.debug("called.");
        try {
            return searchAllEntry();
        } catch (RuntimeException re) {
            log.error("RuntimeException occurred. " + re.getMessage());
            throw re;
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // private methods

    @Transactional
    private List<EntryDto> searchAllEntry() {

        // create the new dto list.
        List<EntryDto> dtoList = new ArrayList<>();

        // get the entities list from repository.
        List<Entry> list = entryRepository.findAll();
        for (Entry entry : list) {
            // map the object and add to dto list.
            EntryDto dto = context.getBean(EntryDto.class);
            dtoList.add(entryUtils.mapEntry(entry, dto));
        }
        return dtoList;
    }

}