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

package org.examproject.blog.controller;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.dozer.Mapper;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import org.examproject.blog.dto.EntryDto;
import org.examproject.blog.form.EntryForm;
import org.examproject.blog.model.EntryModel;
import org.examproject.blog.service.EntryService;
import org.examproject.blog.response.EntryResponse;

/**
 * the entry controller class of the application.
 *
 * @author h.adachi
 */
@Controller
public class EntryController {

    private Logger LOG = LoggerFactory.getLogger(EntryController.class);

    @Inject
    private ApplicationContext context = null;

    @Inject
    private HttpServletRequest request = null;

    @Inject
    private Mapper mapper = null;

    @Inject
    private EntryService entryService = null;

    ///////////////////////////////////////////////////////////////////////////
    // public methods

    /**
     * entry form request.
     * expected HTTP request is '/entry/form.html'
     */
    @RequestMapping(
        value="/entry/form.html",
        method=RequestMethod.GET
    )
    public void doForm(
        @CookieValue(value="__exmp_blog_username", defaultValue="")
        String username,
        @CookieValue(value="__exmp_blog_password", defaultValue="")
        String password,
        @CookieValue(value="__exmp_blog_email", defaultValue="")
        String email,
        Model model
    ) {
        LOG.info("called");
        LOG.info("username:" + username);
        LOG.info("password:" + password);
        LOG.info("email:" + email);

        // create a form-object.
        EntryForm entryForm = context.getBean(EntryForm.class);

        // set the cookie value to the form-object.
        entryForm.setUsername(username);
        entryForm.setPassword(password);
        entryForm.setEmail(email);

        // set the form-object to the model.
        model.addAttribute(
            entryForm
        );
    }

    /**
     * post the entry.
     * expected Ajax HTTP request is '/entry/post.json'
     */
    @RequestMapping(
        value="/entry/post.json",
        method=RequestMethod.POST,
        produces="application/json"
    )
    @ResponseBody
    public EntryResponse doPost(
        @RequestBody
        EntryForm entryForm,
        Model model
    ) {
        LOG.info("called");

        // the response-object will be returned to the html page.

        if (entryForm.getTitle().equals("") || entryForm.getContent().equals("")) {
            throw new RuntimeException("the title and content is must be set.");
        }

        // create a response-object.
        EntryResponse response = context.getBean(EntryResponse.class);

        // get the mapped dto-object using the form-object data.
        EntryDto entryDto = getMappedEntryDto(entryForm);

        // post the entry dto-object using the service-object.
        postEntry(
            entryDto
        );

        // get the list of dto-object from the service-object.
        List<EntryDto> entryDtoList = getEntryDtoList();

        // add to the response-object.
        addToResponse(
            entryDtoList,
            response
        );

        // return the response-object to html page.
        // this will be converted into json.
        return response;
    }

    /**
     * get the entry list.
     * expected Ajax HTTP request is '/entry/list.json'
     */
    @RequestMapping(
        value="/entry/list.json",
        method=RequestMethod.POST,
        produces="application/json"
    )
    @ResponseBody
    public EntryResponse doList(
        @RequestBody
        EntryForm entryForm,
        Model model
    ) {
        LOG.info("called");

        // the response-object will be returned to the html page.

        // create a response-object.
        EntryResponse response = context.getBean(EntryResponse.class);

        // get the mapped dto-object using the form-object data.
        List<EntryDto> entryDtoList = getEntryDtoList();

        // add to the response-object.
        addToResponse(
            entryDtoList,
            response
        );

        // return the response-object to html page.
        // this will be converted into json.
        return response;
    }

    /**
     * delete the entry.
     * expected Ajax HTTP request is '/entry/delete.json'
     */
    @RequestMapping(
        value="/entry/delete.json",
        method=RequestMethod.POST,
        produces="application/json"
    )
    @ResponseBody
    public EntryResponse doDelete(
        @RequestParam(value="code", defaultValue="")
        String code,
        Model model
    ) {
        LOG.info("called");
        LOG.debug("code: " + code);

        // create a response-object.
        EntryResponse response = context.getBean(EntryResponse.class);

        // delete the entry.
        entryService.deleteEntry(
            entryService.getEntryByCode(
                code
            )
        );

        // get the list of dto-object from the service-object.
        List<EntryDto> entryDtoList = getEntryDtoList();

        // add to the response-object.
        addToResponse(
            entryDtoList,
            response
        );

        return response;
    }

    /**
     * if an error is occured, this method will be called.
     */
    @ExceptionHandler
    @ResponseBody
    public EntryResponse handleException(
        Exception e
    ) {
        LOG.info("called");
        LOG.error(e.getMessage());

        // create a response-object.
        EntryResponse response = context.getBean(EntryResponse.class);

        // notify the occurrence of errors to the html page.
        response.setIsError(true);
        return response;
    }

    ///////////////////////////////////////////////////////////////////////////
    // private methods

    /**
     * get the dto-object of the entry using the form-object.
     */
    private EntryDto getMappedEntryDto(
        EntryForm entryForm
    ) {
        LOG.debug("called");

        // create a dto-object.
        EntryDto entryDto = context.getBean(EntryDto.class);

        // map the form-object to the dto-object.
        mapper.map(
            entryForm,
            entryDto
        );

        //LOG.debug(entryDto.getUsername());

        return entryDto;
    }

    /**
     * post the dto-object of the entry using the service-object.
     */
    private void postEntry(
        EntryDto entryDto
    ) {
        LOG.debug("called");

        // save the dto-object.
        entryService.saveEntry(
            entryDto
        );
    }

    /**
     * get the list of dto-obgect from the service-object.
     */
    private List<EntryDto> getEntryDtoList() {
        LOG.debug("called");

        // get the dto-object list from service-object.
        List<EntryDto> entryList = entryService.findAllEntry();

        return entryList;
    }

    /**
     * add to the response-object.
     */
    private void addToResponse(
        List<EntryDto> entryDtoList,
        EntryResponse response
    ) {
        LOG.debug("called");

        // get the server URL of the request.
        StringBuffer fullUrl = request.getRequestURL();
        String serverUrl = fullUrl.toString().split("/entry")[0];

        // create a list of entry object,
        // in order to send to the html page.
        List<EntryModel> entryModelList = new ArrayList<>();

        // process the entry object of all of the list.
        entryDtoList.forEach((EntryDto entryDto) -> {

            // create a object to send to the html page.
            EntryModel entryModel = context.getBean(EntryModel.class);

            // map the value to the object.
            mapper.map(
                entryDto,
                entryModel
            );

            // create the permalink url.
            entryModel.setPermalinkUrl(
                serverUrl + "/entry/" + entryDto.getCode() + ".html"
            );

            // add the object to the object list.
            entryModelList.add(
                entryModel
            );
        });

        // set the object list to response-object.
        response.setEntryModelList(
            entryModelList
        );

        // set the error status.
        response.setIsError(
            false
        );
    }

}
