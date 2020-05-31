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
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import org.examproject.blog.dto.EntryDto;
import org.examproject.blog.entity.Entry;
import org.examproject.blog.entity.Tag;
import org.examproject.blog.repository.TagRepository;

/**
 * @author h.adachi
 */
@Component
public class TagUtils {

    private Logger LOG = LoggerFactory.getLogger(TagUtils.class);

    @Inject
    private ApplicationContext context = null;

    @Inject
    private TagRepository tagRepository = null;

    ///////////////////////////////////////////////////////////////////////////
    // public methods

    /**
     * get the default tag.
     */
    public Tag getDefaultTag() {
        try {
            return tagRepository.findByText("default");
        } catch (Exception e) {
            throw new RuntimeException("an error occurred.", e);
        }
    }

//    /**
//     * get the default tag item set.
//     */
//    public Set<TagItem> getDefaultTagItemSet(
//        Entry entry
//    ){
//        try {
//            Tag tag = getDefaultTag();
//            TagItem tagItem = context.getBean(TagItem.class);
//            tagItem.setEntry(entry);
//            tagItem.setTag(tag);
//             Set<TagItem> tagSet = new HashSet<>();
//            tagSet.add(tagItem);
//            return tagSet;
//        } catch (Exception e) {
//            throw new RuntimeException("an error occurred.", e);
//        }
//    }

//    /**
//     * get the tag item set.
//     */
//    public Set<TagItem> getTagItemSet(
//        EntryDto entryDto,
//        Entry entry
//    ){
//        try {
//            // if the entry is new one.
//            if (entry.getId() == null) {
//                // create the tag items for entity.
//                return getNewTagItemSet(
//                    entryDto,
//                    entry
//                );
//            }
//
//            // if entry is updated.
//
//            // delete the tag items of entity.
//            Set<TagItem> tagSet = entry.getTagItemSet();
//            for (TagItem tagItem : tagSet) {
//                tagItemRepository.delete(tagItem);
//            }
//
//            // create the tag items for entity.
//            return getNewTagItemSet(
//                entryDto,
//                entry
//            );
//        } catch (Exception e) {
//            throw new RuntimeException("an error occurred.", e);
//        }
//    }

    /**
     * get the tags string.
     */
    public String getTagItemString(
        Entry entry
    ){
        try {
            // get the tag items of entity.
            StringBuilder builder = new StringBuilder();
//            Set<Tag> tagSet = entry.getTagSet();
//            for (Tag tag : tagSet) {
//                builder.append(tag.getText());
//                builder.append(" ");
//            }
            return builder.toString();
        } catch (Exception e) {
            throw new RuntimeException("an error occurred.", e);
        }
    }

    /**
     * get the tag set.
     */
    public Set<Tag> getTagSet(
        EntryDto entryDto
    ){
        try {
            Set<Tag> tagSet = new HashSet<>();
            List<String> tags = Arrays.asList(entryDto.getTags().split(" "));
            for (String tagString : tags) {
                String lowerTagString = tagString.toLowerCase();
                // confirm the existence.
                Tag tag = tagRepository.findByText(lowerTagString);
                // create a new tag.
                if (tag == null) {
                    Tag newTag = context.getBean(Tag.class);
                    newTag.setText(lowerTagString);
                    tagRepository.save(newTag);
                    LOG.debug("create the new tag.");
                    tagSet.add(newTag);
                // if already exist set this.
                } else {
                    tagSet.add(tag);
                }
            }
            return tagSet;
        } catch (Exception e) {
            throw new RuntimeException("an error occurred.", e);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // private methods

//    /**
//     * get the new tag item set.
//     */
//    private Set<TagItem> getNewTagItemSet(
//        EntryDto entryDto,
//        Entry entry
//    ){
//        try {
//            Set<TagItem> tagSet = new HashSet<>();
//            List<String> tags = Arrays.asList(entryDto.getTags().split(" "));
//            for (String tagString : tags) {
//                String lowerTagString = tagString.toLowerCase();
//                // confirm the existence.
//                Tag tag = tagRepository.findByText(lowerTagString);
//                // create a new tag.
//                if (tag == null) {
//                    Tag newTag = context.getBean(Tag.class);
//                    newTag.setText(lowerTagString);
//                    tagRepository.save(newTag);
//                    LOG.debug("create the new tag.");
//                    TagItem tagItem = context.getBean(TagItem.class);
//                    tagItem.setEntry(entry);
//                    tagItem.setTag(newTag);
//                    tagSet.add(tagItem);
//                // if already exist set this.
//                } else {
//                    TagItem tagItem = context.getBean(TagItem.class);
//                    tagItem.setEntry(entry);
//                    tagItem.setTag(tag);
//                    tagSet.add(tagItem);
//                }
//            }
//            return tagSet;
//        } catch (Exception e) {
//            throw new RuntimeException("an error occurred.", e);
//        }
//    }

}
