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

package org.expt.blog.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import org.expt.blog.dto.EntryDto;
import org.expt.blog.model.FeedModel;
import org.expt.blog.service.FeedService;

/**
 * the feed controller class of the application.
 *
 * @author h.adachi
 */
@Slf4j
@RequiredArgsConstructor
@Controller
public class FeedController {

    @NonNull
    private final ApplicationContext context;

    @NonNull
    private final HttpServletRequest request;

    @NonNull
    private final FeedService feedService;

    ///////////////////////////////////////////////////////////////////////////
    // public methods

    /**
     * rss feed request.
     * expected HTTP request is '/entry/feed.rss'
     */
    @RequestMapping(
        value="/entry/feed.rss",
        method=RequestMethod.GET
    )
    public ModelAndView getFeedInRss() {
        log.info("called");

        // create the model-view object of spring mvc.
        val mv = new ModelAndView();

        // set the view name for render.
        mv.setViewName(
            "rssFeedView"
        );

        // set the model-object to render.
        mv.addObject(
            "feedModel",
            // get the feed-model object list.
            getFeedModelList()
        );

        // call the "rssFeedView" view.
        return mv;
    }

    /**
     * atom feed request.
     * expected HTTP request is '/entry/feed.atom'
     */
    @RequestMapping(
        value="/entry/feed.atom",
        method=RequestMethod.GET
    )
    public ModelAndView getFeedInAtom() {
        log.info("called");

        // create the model-view object of spring mvc.
        val mv = new ModelAndView();

        // set the view name for render.
        mv.setViewName(
            "atomFeedView"
        );

        // set the model-object to render.
        mv.addObject(
            "feedModel",
            // get the feed-model object list.
            getFeedModelList()
        );

        // call the "atomFeedView" view.
        return mv;
    }

    ///////////////////////////////////////////////////////////////////////////
    // private methods

    private List<FeedModel> getFeedModelList() {

        List<FeedModel> feedModelList = new ArrayList<FeedModel>();

        // get the dto-object list from service-object.
        List<EntryDto> entryDtoList = feedService.findAllEntry();

        // get the server URL of the request.
        StringBuffer fullUrl = request.getRequestURL();
        String serverUrl = fullUrl.toString().split("/entry")[0];

        // process the entry object of all of the list.
        for (EntryDto entryDto : entryDtoList) {

            // create a object to build to the feed.
            val feedModel = context.getBean(FeedModel.class);

            // set the value to the object.
            feedModel.setTitle(
                entryDto.getTitle()
            );
            feedModel.setSummary(
                entryDto.getContent()
            );
            feedModel.setCreatedDate(
                entryDto.getCreated()
            );

            // create the permalink url.
            feedModel.setUrl(
               serverUrl + "/entry/" + entryDto.getCode() + ".html"
            );

            // add the object to the object list.
            feedModelList.add(
                feedModel
            );
        }

        return feedModelList;
    }
}
