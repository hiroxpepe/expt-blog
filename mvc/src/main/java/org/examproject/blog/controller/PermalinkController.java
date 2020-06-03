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

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.dozer.Mapper;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.examproject.blog.dto.EntryDto;
import org.examproject.blog.model.EntryModel;
import org.examproject.blog.service.EntryService;

/**
 * the entry permalink controller class of the application.
 *
 * @author h.adachi
 */
@Slf4j
@RequiredArgsConstructor
@Controller
public class PermalinkController {

    @NonNull
    private final ApplicationContext context;

    @NonNull
    private final Mapper mapper;

    @NonNull
    private final EntryService entryService;

    ///////////////////////////////////////////////////////////////////////////
    // public methods

    /**
     * permalink request.
     * expected HTTP request is 'http://localhost:8080/entry/nnnnnnnnn.html'
     */
    @RequestMapping(
        value="/entry/{code}.html",
        method=RequestMethod.GET
    )
    public String doGet(
        @PathVariable String code,
        Model model
    ) {
        log.info("called");
        log.debug("code: " + code);

        // set the model-object to the model.
        model.addAttribute(
            getEntryModel(code)
        );

        return "entry/permalink";
    }

    ///////////////////////////////////////////////////////////////////////////
    // private methods

    /**
     * get the model-object from service-object.
     */
    private EntryModel getEntryModel(
        String code
    ) {
        log.debug("called");

        // get a dto-object from service-object.
        EntryDto entryDto = entryService.getEntryByCode(code);

        // create a model-object.
        val entryModel = context.getBean(EntryModel.class);

        // map the dto-object to the model-object.
        mapper.map(
            entryDto,
            entryModel
        );

        return entryModel;
    }

}
