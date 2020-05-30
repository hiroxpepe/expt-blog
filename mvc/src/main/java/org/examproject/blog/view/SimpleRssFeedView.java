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

//import com.sun.syndication.feed.rss.Channel;
//import com.sun.syndication.feed.rss.Content;
//import com.sun.syndication.feed.rss.Item;

import com.rometools.rome.feed.rss.Channel;
import com.rometools.rome.feed.rss.Content;
import com.rometools.rome.feed.rss.Item;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.feed.AbstractRssFeedView;

import org.examproject.blog.model.FeedModel;

/**
 * the rss feed view class of the application.
 * @author hiroxpepe
 */
public class SimpleRssFeedView extends AbstractRssFeedView {

    private Logger LOG = LoggerFactory.getLogger(SimpleRssFeedView.class);

    @Override
    protected void buildFeedMetadata(
        Map<String, Object> model,
        Channel feed,
        HttpServletRequest request
    ) {
        // set the status.
        feed.setTitle("exmpblog");
        feed.setDescription("examproject.org - exmpblog");
        feed.setLink("http://examproject.org");

        super.buildFeedMetadata(
            model,
            feed,
            request
        );
    }

    @Override
    protected List<Item> buildFeedItems(
        Map<String, Object> model,
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        // get the entry list.
        List<FeedModel> feedModelList = (List<FeedModel>) model.get(
            "feedModel"
        );
        List<Item> itemList = new ArrayList<>(
            feedModelList.size()
        );

        // create the entries.
        for (FeedModel feedModel : feedModelList ) {
            Item item = new Item();
            Content content = new Content();
            content.setValue(feedModel.getSummary());
            item.setContent(content);
            item.setTitle(feedModel.getTitle());
            item.setLink(feedModel.getUrl());
            item.setPubDate(feedModel.getCreatedDate());
            itemList.add(
                item
            );
        }

        // set the charset of UTF-8 to the response header.
        response.setContentType(
            "application/rss+xml; charset=UTF-8"
        );

        return itemList;
    }

}
