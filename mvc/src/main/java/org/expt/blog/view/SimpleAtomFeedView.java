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

package org.expt.blog.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rometools.rome.feed.atom.Content;
import com.rometools.rome.feed.atom.Entry;
import com.rometools.rome.feed.atom.Feed;
import com.rometools.rome.feed.atom.Link;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.servlet.view.feed.AbstractAtomFeedView;

import org.expt.blog.model.FeedModel;

/**
 * the atom feed view class of the application.
 * @author h.adachi
 */
@Slf4j
public class SimpleAtomFeedView extends AbstractAtomFeedView {

    @Override
    protected void buildFeedMetadata(
        Map<String, Object> model,
        Feed feed,
        HttpServletRequest request
    ) {
        // set the status.
        feed.setTitle("expt-blog");
        val description = new Content();
        description.setValue("expt.org - expt-blog");
        feed.setSubtitle(description);
        List<Link> links = new ArrayList<>();
        val link = new Link();
        link.setHref("http://expt.org");
        links.add(link);
        feed.setAlternateLinks(links);

        super.buildFeedMetadata(
            model,
            feed,
            request
        );
    }

    @Override
    protected List<Entry> buildFeedEntries(
        Map<String, Object> model,
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        // get the entry list.
        List<FeedModel> feedModelList = (List<FeedModel>) model.get(
            "feedModel"
        );
        List<Entry> entryList = new ArrayList<>(
            feedModelList.size()
        );

        // create the entries.
        for (FeedModel feedModel : feedModelList ) {
            val entry = new Entry();
            val content = new Content();
            content.setValue(feedModel.getSummary());
            val summary = new Content();
            summary.setValue(content.getValue());
            entry.setSummary(summary);
            entry.setTitle(feedModel.getTitle());
            List<Link> links = new ArrayList<>();
            val link = new Link();
            link.setHref(feedModel.getUrl());
            links.add(link);
            entry.setAlternateLinks(links);
            entry.setPublished(feedModel.getCreatedDate());
            entryList.add(
                entry
            );
        }

        // set the charset of UTF-8 to the response header.
        response.setContentType(
            "application/atom+xml; charset=UTF-8"
        );

        return entryList;
    }
}
