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

package org.expt.blog.functor;

import java.util.List;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.Transformer;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import org.expt.blog.dto.EntryDto;
import org.expt.blog.entity.Entry;
import org.expt.blog.repository.EntryRepository;
import org.expt.blog.util.EntryUtils;

/**
 * @author h.adachi
 */
@Slf4j
@RequiredArgsConstructor
public class CodeToEntryTransformer implements Transformer {

    @NonNull
    private final ApplicationContext context;

    @NonNull
    private final EntryRepository entryRepository;

    @NonNull
    private final EntryUtils entryUtils;

    ///////////////////////////////////////////////////////////////////////////
    // public methods

    @Override
    public Object transform(Object o) {
        log.debug("called.");
        try {
            return getEntryDto(o);
        } catch (Exception e) {
            throw new RuntimeException("an error occurred.", e);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // private methods

    @Transactional
    private EntryDto getEntryDto(
        Object o
    ){
        // if 'code' is offered, find the entity from repository, and mapping to dto.
        if (o != null) {
            // get the entity from repository.
            String code = o.toString();
            List<Entry> entryList = entryRepository.findByCode(code);
            if (entryList.isEmpty()) {
                return null;
            }

            // TODO: if the code is not unique
            Entry entry = entryList.get(0);

            // return a mapped dto.
            EntryDto dto = context.getBean(EntryDto.class);
            return entryUtils.mapEntry(entry, dto);

        } else {
            // return null..
            return null;
        }
    }
}
