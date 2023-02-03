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

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.Closure;
import org.springframework.transaction.annotation.Transactional;

import org.expt.blog.dto.EntryDto;
import org.expt.blog.util.EntryUtils;

/**
 * @author h.adachi
 */
@Slf4j
@RequiredArgsConstructor
public class DeleteEntryClosure implements Closure {

    @NonNull
    private final EntryUtils entryUtils;

    ///////////////////////////////////////////////////////////////////////////
    // public methods

    @Override
    public void execute(Object o) {
        log.debug("called.");
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
