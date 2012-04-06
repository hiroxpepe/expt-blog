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

package org.examproject.blog.controller

import java.util.ArrayList
import java.util.Date
import java.util.List
import javax.inject.Inject

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView

import org.examproject.blog.dto.EntryDto
import org.examproject.blog.model.FeedModel
import org.examproject.blog.service.FeedService

import scala.collection.JavaConversions._

/**
 * the feed controller class of the application.
 *
 * @author hiroxpepe
 */
@Controller
class FeedController {
 
    private val LOG: Logger = LoggerFactory.getLogger(
        classOf[FeedController]
    )
    
    @Inject
    private val context: ApplicationContext = null

    @Inject
    private val feedService: FeedService = null
    
    ///////////////////////////////////////////////////////////////////////////
    // public methods
    
    ///////////////////////////////////////////////////////////////////////////
    /**
     * rss feed request.
     * expected HTTP request is '/entry/feed.rss'
     */
    @RequestMapping(
        value=Array("/entry/feed.rss"),
        method=Array(RequestMethod.GET)
    )
    def getFeedInRss()
    : ModelAndView = {
        LOG.info("called")
        
        // create the model-view object of spring mvc.
        val mav: ModelAndView = new ModelAndView()
        
        // set the view name for render.
        mav.setViewName(
            "rssFeedView"
        )
        
        // set the model-object to render.
        mav.addObject(
            "feedModel",
            // get the feed-model object list.
            getFeedModelList()
        )

        return mav
    }
    
    ///////////////////////////////////////////////////////////////////////////
    /**
     * atom feed request.
     * expected HTTP request is '/entry/feed.atom'
     */
    @RequestMapping(
        value=Array("/entry/feed.atom"),
        method=Array(RequestMethod.GET)
    )
    def getFeedInAtom()
    : ModelAndView = {
        LOG.info("called")
        
        // create the model-view object of spring mvc.
        val mav: ModelAndView = new ModelAndView()
        
        // set the view name for render.
        mav.setViewName(
            "atomFeedView"
        )
        
        // set the model-object to render.
        mav.addObject(
            "feedModel",
            // get the feed-model object list.
            getFeedModelList()
        )

        return mav
    }
    
    ///////////////////////////////////////////////////////////////////////////
    // private methods
    
    private def getFeedModelList()
    : List[FeedModel] = {
        
        val feedModelList: List[FeedModel] = new ArrayList[FeedModel]()
        
        // get the dto-object list from service-object.
        val entryDtoList: List[EntryDto] = feedService.findAllEntry()
        
        // process the entry object of all of the list.
        for (entryDto: EntryDto <- entryDtoList) {
            
            // create a object to build to the feed.
            val feedMode: FeedModel = context.getBean(
                classOf[FeedModel]
            )
            
            // set the value to the object.
            feedMode.setTitle(
                entryDto.getTitle()
            )
            feedMode.setSummary(
                entryDto.getContent()
            )
            feedMode.setCreatedDate(
                entryDto.getCreated()
            )
            
            // this is a still mock..
            feedMode.setUrl(
                "http://localhost/" + entryDto.getTitle() + ".html"
            )
            
            // add the object to the object list.
            feedModelList.add(
                feedMode
            )
        }
        
        return feedModelList
    } 

}
