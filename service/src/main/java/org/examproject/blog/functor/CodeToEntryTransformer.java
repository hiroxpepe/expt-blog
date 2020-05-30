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

import java.util.List;
import javax.inject.Inject;

import org.apache.commons.collections.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import org.examproject.blog.dto.EntryDto;
import org.examproject.blog.entity.Entry;
import org.examproject.blog.repository.EntryRepository;
import org.examproject.blog.util.EntryUtils;

/**
 * @author h.adachi
 */
public class CodeToEntryTransformer implements Transformer {

    private Logger LOG = LoggerFactory.getLogger(CodeToEntryTransformer.class);

    @Inject
    private ApplicationContext context = null;

    @Inject
    private EntryRepository entryRepository = null;

    @Inject
    private EntryUtils entryUtils = null;

    ///////////////////////////////////////////////////////////////////////////
    // public methods

    @Override
    public Object transform(Object o) {
        LOG.debug("called.");
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