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

package org.examproject.blog.service;

import java.lang.Long;
import java.util.List;

import lombok.AllArgsConstructor;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.Factory;
import org.apache.commons.collections.Transformer;

import org.examproject.blog.dto.EntryDto;

/**
 * @author hiroxpepe
 */
@AllArgsConstructor
public class SimpleEntryService implements EntryService {

    // there is nothing of a logic here, it's just an adapter class..

    private Transformer idToEntryTransformer;

    private Transformer codeToEntryTransformer;

    private Factory allEntryFactory;

    private Closure saveEntryClosure;

    private Closure deleteEntryClosure;

    @Override
    public EntryDto getEntryById(Long id) {
        return (EntryDto) idToEntryTransformer.transform(id);
    }

    @Override
    public EntryDto getEntryByCode(String code) {
        return (EntryDto) codeToEntryTransformer.transform(code);
    }

    @Override
    public List<EntryDto> findAllEntry(){
        return (List<EntryDto>) allEntryFactory.create();
    }

    @Override
    public void saveEntry(EntryDto entryDto) {
        saveEntryClosure.execute(entryDto);
    }

    @Override
    public void deleteEntry(EntryDto entryDto) {
        deleteEntryClosure.execute(entryDto);
    }

}
