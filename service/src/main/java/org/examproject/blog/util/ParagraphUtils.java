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

import java.util.Date;
import java.util.Set;
import java.util.HashSet;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import org.examproject.blog.dto.EntryDto;
import org.examproject.blog.entity.Entry;
import org.examproject.blog.entity.Paragraph;
import org.examproject.blog.repository.ParagraphRepository;

/**
 * @author h.adachi
 */
@Component
public class ParagraphUtils {

    private Logger LOG = LoggerFactory.getLogger(ParagraphUtils.class);

    @Inject
    private ApplicationContext context = null;

    @Inject
    private ParagraphRepository paragraphRepository = null;

    ///////////////////////////////////////////////////////////////////////////
    // public methods

    ///////////////////////////////////////////////////////////////////////////
    /**
     * get the paragraph set.
     */
    public Set<Paragraph> getParagraphSet(
        EntryDto entryDto,
        Entry entry
    ){
        try {
            Paragraph titleParagraph = getTitle(entryDto, entry);
            Paragraph contentParagraph = getContent(entryDto, entry);
            Set<Paragraph> paragraphSet = new HashSet<>();
            paragraphSet.add(titleParagraph);
            paragraphSet.add(contentParagraph);
            return paragraphSet;
        } catch (Exception e) {
            throw new RuntimeException("an error occurred.", e);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    /**
     * get the title string.
     */
    public String getTitleString(
        Entry entry
    ){
        try {
            Set<Paragraph> paragraphSet = entry.getParagraphSet();
            for (Paragraph paragraph : paragraphSet) {
                if (paragraph.getKey().equals("title")) {
                    return paragraph.getContent();
                }
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("an error occurred.", e);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    /**
     * get the title string.
     */
    public String getContentString(
        Entry entry
    ){
        try {
            Set<Paragraph> paragraphSet =  entry.getParagraphSet();
            for (Paragraph paragraph : paragraphSet) {
                if (paragraph.getKey().equals("content")) {
                    return paragraph.getContent();
                }
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("an error occurred.", e);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // private methods

    ///////////////////////////////////////////////////////////////////////////
    /**
     * get the title paragraph.
     */
    private Paragraph getTitle(
        EntryDto entryDto,
        Entry entry
    ){
        try {
            if (entry.getId() == null) {
                Paragraph newTitle = context.getBean(Paragraph.class);
                newTitle.setContent(entryDto.getTitle());
                newTitle.setKey("title");
                newTitle.setCreated(new Date());
                newTitle.setUpdated(new Date());
                newTitle.setEntry(entry);
                return newTitle;
            }
            Paragraph title = paragraphRepository.findByEntryAndKey(entry, "title");
            title.setContent(entryDto.getTitle());
            title.setUpdated(new Date());
            return title;
        } catch (Exception e) {
            throw new RuntimeException("an error occurred.", e);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    /**
     * get the content paragraph.
     */
    private Paragraph getContent(
        EntryDto entryDto,
        Entry entry
    ){
        try {
            if (entry.getId() == null) {
                Paragraph newContent = context.getBean(Paragraph.class);
                newContent.setContent(entryDto.getContent());
                newContent.setKey("content");
                newContent.setCreated(new Date());
                newContent.setUpdated(new Date());
                newContent.setEntry(entry);
                return newContent;
            }
            Paragraph content = paragraphRepository.findByEntryAndKey(entry, "content");
            content.setContent(entryDto.getContent());
            content.setUpdated(new Date());
            return content;
        } catch (Exception e) {
            throw new RuntimeException("an error occurred.", e);
        }
    }

}
