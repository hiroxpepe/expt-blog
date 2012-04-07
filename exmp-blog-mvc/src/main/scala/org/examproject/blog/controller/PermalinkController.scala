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
import java.util.List
import javax.inject.Inject
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.dozer.Mapper
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody

import org.examproject.blog.dto.EntryDto
import org.examproject.blog.form.EntryForm
import org.examproject.blog.service.EntryService
import org.examproject.blog.response.AjaxResponse
import org.examproject.blog.response.Entry

import scala.collection.JavaConversions._

/**
 * the entry permalink controller class of the application.
 *
 * @author hiroxpepe
 */
@Controller
class PermalinkController {
    
    private val LOG: Logger = LoggerFactory.getLogger(
        classOf[PermalinkController]
    )
    
    @Inject
    private val context: ApplicationContext = null

    @Inject
    private val mapper: Mapper = null

    @Inject
    private val entryService: EntryService = null
    
    ///////////////////////////////////////////////////////////////////////////
    // public methods
    
    ///////////////////////////////////////////////////////////////////////////
    /**
     * permalink request.
     * expected HTTP request is 'http://localhost:8080/entry/2012/04/06/nnnnnnnnn.html'
     */
    @RequestMapping(
        value=Array("/entry/{year}/{month}/{day}/{code}.html"),
        method=Array(RequestMethod.GET)
    )
    def doGet(
        @PathVariable year: String,
        @PathVariable month: String,
        @PathVariable day: String,
        @PathVariable code: String,
        model: Model
    ): String = {
        LOG.info("called")
        
        LOG.debug("year: " + year)
        LOG.debug("month: " + month)
        LOG.debug("day: " + day)
        LOG.debug("code: " + code)
        
        // get the dto-object from service-object.
        val entryDto: EntryDto = entryService.getEntryByCode(
            code
        )
    
        // create a form-object.
        val entryForm: EntryForm = context.getBean(
            classOf[EntryForm]
        )
    
        // map the dto-object to the form-object.
        mapper.map(
            entryDto,
            entryForm
        )
     
        // set the form-object to the model. 
        model.addAttribute(
            entryForm
        )
        
        return "entry/permalink";
    }
    
}
