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

package org.examproject.blog.view

import java.util.ArrayList
import java.util.List
import java.util.Map
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import com.sun.syndication.feed.rss.Channel
import com.sun.syndication.feed.rss.Content
import com.sun.syndication.feed.rss.Item

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.servlet.view.feed.AbstractRssFeedView

import org.examproject.blog.model.FeedModel

import scala.collection.JavaConversions._

/**
 * the rss feed view class of the application.
 * @author hiroxpepe
 */
class SimpleRssFeedView extends AbstractRssFeedView {
 
    private val LOG: Logger = LoggerFactory.getLogger(
        classOf[SimpleRssFeedView]
    )
    
    @Override
    override def buildFeedMetadata(
        model: Map[String, Object],
        feed: Channel,
        request: HttpServletRequest 
    )
    : Unit = {
        
        // set the status.
        feed.setTitle("exmpblog")
        feed.setDescription("examproject.org - exmpblog")
        feed.setLink("http://examproject.org")
        
        super.buildFeedMetadata(
            model,
            feed,
            request
        )
    }
 
    @Override
    override def buildFeedItems(
        model: Map[String, Object],
        request: HttpServletRequest,
        response: HttpServletResponse
    )
    : List[Item] = {
        
        // get the entry list.
        val feedModelList: List[FeedModel] = model.get(
            "feedModel"
        ).asInstanceOf[List[FeedModel]]
        val itemList: List[Item] = new ArrayList[Item](
            feedModelList.size()
        )

        // create the entries.
        for (val feedModel: FeedModel <- feedModelList ) {
            val item: Item = new Item()
            val content: Content = new Content()
            content.setValue(feedModel.getSummary())
            item.setContent(content)
            item.setTitle(feedModel.getTitle())
            item.setLink(feedModel.getUrl())
            item.setPubDate(feedModel.getCreatedDate())
            itemList.add(
                item
            )
        }
        
        // set the charset of UTF-8 to the response header. 
        response.setContentType(
            "application/rss+xml; charset=UTF-8"
        );

        return itemList;
    }
 
}
