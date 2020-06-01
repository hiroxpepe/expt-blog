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

import javax.inject.Inject;

import org.apache.commons.collections.Closure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import org.examproject.blog.dto.EntryDto;
import org.examproject.blog.util.EntryUtils;

/**
 * @author h.adachi
 */
public class DeleteEntryClosure implements Closure {

    private Logger LOG = LoggerFactory.getLogger(DeleteEntryClosure.class);

    @Inject
    private final EntryUtils entryUtils = null;

    ///////////////////////////////////////////////////////////////////////////
    // public methods

    @Override
    public void execute(Object o) {
        LOG.debug("called.");
        EntryDto entryDto = (EntryDto) o;
        try {
            delete(entryDto);
        } catch (Exception e) {
            throw new RuntimeException("an error occurred.", e);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // private methods

    @Transactional
    private void delete(
        EntryDto entryDto
    ) {
        try {
            // delete the entry.
            entryUtils.deleteEntry(entryDto);
        } catch (Exception e) {
            throw new RuntimeException("an error occurred.", e);
        }
    }

}
