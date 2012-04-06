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

import com.sun.syndication.feed.atom.Content
import com.sun.syndication.feed.atom.Entry
import com.sun.syndication.feed.atom.Feed
import com.sun.syndication.feed.atom.Link

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.servlet.view.feed.AbstractAtomFeedView

import org.examproject.blog.model.FeedModel

import scala.collection.JavaConversions._

/**
 * the atom feed view class of the application.
 * @author hiroxpepe
 */
class SimpleAtomFeedView extends AbstractAtomFeedView {
 
    private val LOG: Logger = LoggerFactory.getLogger(
        classOf[SimpleAtomFeedView]
    )
    
    @Override
    override def buildFeedMetadata(
        model: Map[String, Object],
        feed: Feed,
        request: HttpServletRequest 
    )
    : Unit = {
        
        // set the status.
        feed.setTitle("exmpblog")
        val description: Content = new Content()
        description.setValue("examproject.org - exmpblog")
        feed.setSubtitle(description)      
        val links: List[Link] = new ArrayList[Link]()
        val link: Link = new Link()
        link.setHref("http://examproject.org")
        links.add(link)
        feed.setAlternateLinks(links)
        
        super.buildFeedMetadata(
            model,
            feed,
            request
        )
    }
 
    @Override
    override def buildFeedEntries(
        model: Map[String, Object],
        request: HttpServletRequest,
        response: HttpServletResponse
    )
    : List[Entry] = {
        
        // get the entry list.
        val feedModelList: List[FeedModel] = model.get(
            "feedModel"
        ).asInstanceOf[List[FeedModel]]
        val entryList: List[Entry] = new ArrayList[Entry](
            feedModelList.size()
        )

        // create the entries.
        for (val feedModel: FeedModel <- feedModelList ) {
            val entry: Entry = new Entry()
            val content: Content = new Content()
            content.setValue(feedModel.getSummary())     
            val summary: Content = new Content()
            summary.setValue(content.getValue())
            entry.setSummary(summary);
            entry.setTitle(feedModel.getTitle())
            val links: List[Link] = new ArrayList[Link]()
            val link: Link = new Link()
            link.setHref(feedModel.getUrl())
            links.add(link)
            entry.setAlternateLinks(links)
            entry.setPublished(feedModel.getCreatedDate())
            entryList.add(
                entry
            )
        }
        
        // set the charset of UTF-8 to the response header. 
        response.setContentType(
            "application/atom+xml; charset=UTF-8"
        );

        return entryList;
    }
 
}
