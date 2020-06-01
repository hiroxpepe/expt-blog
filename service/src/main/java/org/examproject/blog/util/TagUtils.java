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

import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import org.examproject.blog.entity.Entry;
import org.examproject.blog.entity.EntryTag;
import org.examproject.blog.entity.Tag;
import org.examproject.blog.repository.EntryRepository;
import org.examproject.blog.repository.EntryTagRepository;
import org.examproject.blog.repository.TagRepository;

/**
 * @author h.adachi
 */
@Component
public class TagUtils {

    private Logger LOG = LoggerFactory.getLogger(TagUtils.class);

    @Inject
    private final ApplicationContext context = null;

    @Inject
    private final TagRepository tagRepository = null;

    @Inject
    private final EntryTagRepository entryTagRepository = null;

    ///////////////////////////////////////////////////////////////////////////
    // public methods

    /**
     * get the tags string.
     */
    public String getTagItemString(
        Entry entry
    ){
        try {
            // get tags string of the entity.
            StringBuilder builder = new StringBuilder();
            //List<EntryTag> entryTagList = entryTagRepository.findByEntryId(entry.getId()); // ここで 0
            List<EntryTag> entryTagList = entryTagRepository.findByEntry(entry); // ここで 0
            for (EntryTag entryTag : entryTagList) {
                builder.append(entryTag.getTag().getText());
                builder.append(" ");
            }
            return builder.toString();
        } catch (Exception e) {
            LOG.error(ExceptionUtils.getStackTrace(e));
            throw new RuntimeException("an error occurred.", e);
        }
    }

    // FIXME: get the entryTag set from enrty itself!

    /**
     * get the entrytag set. * arg of the entry reference itself must be required! *
     */
    public Set<EntryTag> getEntryTagSet(
        Entry entry, String tags
    ){
        try {
            Set<EntryTag> entryTagSet = new HashSet<>();
            List<String> tagList = Arrays.asList(tags.split(" "));
            for (String tagString : tagList) {
                EntryTag entryTag = null;
                String lowerTagString = tagString.toLowerCase();
                // confirm the existence of the tag.
                Tag tag = tagRepository.findByText(lowerTagString); // ここでおかしい？
                // the tag is completely new one.
                if (tag == null) {
                    tag = context.getBean(Tag.class);
                    tag.setText(tagString);
                    tagRepository.save(tag); // MEMO
                    LOG.debug("save tag.");
                    entryTag = context.getBean(EntryTag.class);
                    entryTag.setEntry(entry);
                    entryTag.setTag(tag);
                    entryTagRepository.save(entryTag); // MEMO
                    LOG.debug("save entryTag.");
                // the tag already exists.
                } else {
                    // confirm the existence of the entry's tag.
                    //List<EntryTag> entryTagList = entryTagRepository.findByEntryIdAndTagId(entry.getId(), tag.getId());
                    List<EntryTag> entryTagList = entryTagRepository.findByEntryAndTag(entry, tag);
                    // this tag is new one for the entry.
                    if (entryTagList == null) {
                        entryTag = context.getBean(EntryTag.class);
                        entryTag.setEntry(entry);
                        entryTag.setTag(tag);
                        entryTagRepository.save(entryTag); // MEMO
                        LOG.debug("save entryTag.");
                    // already exist a pair of the entry and the tag.
                    } else {
                        entryTag = entryTagList.get(0);
                    }
                }
                entryTagSet.add(entryTag);
            }
            return entryTagSet;
        } catch (Exception e) {
            LOG.error(ExceptionUtils.getStackTrace(e));
            throw new RuntimeException("an error occurred.", e);
        }
    }

}
