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

import java.lang.Long;
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
 * @author hiroxpepe
 */
public class IdToEntryTransformer implements Transformer {

    private Logger LOG = LoggerFactory.getLogger(IdToEntryTransformer.class);

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
        // if 'id' is offered, find the entity from repository, and mapping to dto.
        if (o != null) {
            // get the entity from repository.
            Long id = Long.valueOf(o.toString());
            //val entry: Entry = entryRepository.findOne(id).asInstanceOf<Entry>;
            Entry entry = entryRepository.getOne(id);

            // return a mapped dto.
            EntryDto dto = context.getBean(EntryDto.class);
            return entryUtils.mapEntry(entry, dto);

        // if the new request, a null 'id' will be provided.
        } else {
            // return a new dto.
            return context.getBean(EntryDto.class);
        }
    }

}
