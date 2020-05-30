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

package org.examproject.blog.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rometools.rome.feed.atom.Content;
import com.rometools.rome.feed.atom.Entry;
import com.rometools.rome.feed.atom.Feed;
import com.rometools.rome.feed.atom.Link;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.feed.AbstractAtomFeedView;

import org.examproject.blog.model.FeedModel;

/**
 * the atom feed view class of the application.
 * @author h.adachi
 */
public class SimpleAtomFeedView extends AbstractAtomFeedView {

    private Logger LOG = LoggerFactory.getLogger(SimpleAtomFeedView.class);

    @Override
    protected void buildFeedMetadata(
        Map<String, Object> model,
        Feed feed,
        HttpServletRequest request
    ) {
        // set the status.
        feed.setTitle("exmp-blog");
        Content description = new Content();
        description.setValue("examproject.org - exmp-blog");
        feed.setSubtitle(description);
        List<Link> links = new ArrayList<>();
        Link link = new Link();
        link.setHref("http://examproject.org");
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
            Entry entry = new Entry();
            Content content = new Content();
            content.setValue(feedModel.getSummary());
            Content summary = new Content();
            summary.setValue(content.getValue());
            entry.setSummary(summary);
            entry.setTitle(feedModel.getTitle());
            List<Link> links = new ArrayList<>();
            Link link = new Link();
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
