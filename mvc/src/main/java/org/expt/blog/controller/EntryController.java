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
import org.apache.commons.lang.exception.ExceptionUtils;
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

import org.expt.blog.dto.EntryDto;
import org.expt.blog.form.EntryForm;
import org.expt.blog.model.EntryModel;
import org.expt.blog.service.EntryService;
import org.expt.blog.response.EntryResponse;

/**
 * the entry controller class of the application.
 *
 * @author h.adachi
 */
@Slf4j
@RequiredArgsConstructor
@Controller
public class EntryController {

    @NonNull
    private final ApplicationContext context;

    @NonNull
    private final HttpServletRequest request;

    @NonNull
    private final Mapper mapper;

    @NonNull
    private final EntryService entryService;

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
        @CookieValue(value="__expt_blog_username", defaultValue="anonymous")
        String username,
        @CookieValue(value="__expt_blog_password", defaultValue="password")
        String password,
        @CookieValue(value="__expt_blog_email", defaultValue="example@email.com")
        String email,
        Model model
    ) {
        log.info("called");

        // create a form-object.
        val entryForm = context.getBean(EntryForm.class);

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
     * get the entry list.
     * expected Ajax HTTP request is '/entry/list.json' and always called in initialized.
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
        log.info("called");

        // the response-object will be returned to the html page.

        // create a response-object.
        val response = context.getBean(EntryResponse.class);

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
        log.info("called");

        // the response-object will be returned to the html page.

        if (entryForm.getTitle().equals("") || entryForm.getContent().equals("")) {
            throw new RuntimeException("the title and content is must be set.");
        }

        // create a response-object.
        val response = context.getBean(EntryResponse.class);

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
        log.info("called");
        log.debug("code: " + code);

        // create a response-object.
        val response = context.getBean(EntryResponse.class);

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
        log.info("called");
        log.error(ExceptionUtils.getStackTrace(e));

        // create a response-object.
        val response = context.getBean(EntryResponse.class);

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
        log.debug("called");

        // create a dto-object.
        val entryDto = context.getBean(EntryDto.class);

        // map the form-object to the dto-object.
        mapper.map(
            entryForm,
            entryDto
        );

        return entryDto;
    }

    /**
     * post the dto-object of the entry using the service-object.
     */
    private void postEntry(
        EntryDto entryDto
    ) {
        log.debug("called");

        // save the dto-object.
        entryService.saveEntry(
            entryDto
        );
    }

    /**
     * get the list of dto-obgect from the service-object.
     */
    private List<EntryDto> getEntryDtoList() {
        log.debug("called");

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
        log.debug("called");

        // get the server URL of the request.
        StringBuffer fullUrl = request.getRequestURL();
        String serverUrl = fullUrl.toString().split("/entry")[0];

        // create a list of entry object,
        // in order to send to the html page.
        List<EntryModel> entryModelList = new ArrayList<>();

        // process the entry object of all of the list.
        entryDtoList.forEach((EntryDto entryDto) -> {

            // create a object to send to the html page.
            val entryModel = context.getBean(EntryModel.class);

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
