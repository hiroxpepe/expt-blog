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
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody

import org.examproject.blog.dto.EntryDto
import org.examproject.blog.form.EntryForm
import org.examproject.blog.model.EntryModel
import org.examproject.blog.service.EntryService
import org.examproject.blog.response.AjaxResponse

import scala.collection.JavaConversions._

/**
 * the entry controller class of the application.
 *
 * @author hiroxpepe
 */
@Controller
class EntryController {
    
    private val LOG: Logger = LoggerFactory.getLogger(
        classOf[EntryController]
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
     * entry form request.
     * expected HTTP request is '/entry/form.html'
     */
    @RequestMapping(
        value=Array("/entry/form.html"),
        method=Array(RequestMethod.GET)
    )
    def doForm(
        @CookieValue(value="__exmp_blog_username", defaultValue="")
        username: String,
        @CookieValue(value="__exmp_blog_password", defaultValue="")
        password: String,
        @CookieValue(value="__exmp_blog_author", defaultValue="")
        author: String,
        model: Model
    ) = {
        LOG.info("called")
             
        // create a form-object.
        val entryForm: EntryForm = context.getBean(
            classOf[EntryForm]
        )
        
        // set the cookie value to the form-object.
        entryForm.setUsername(username)
        entryForm.setPassword(password)
        entryForm.setAuthor(author)
        
        // set the form-object to the model. 
        model.addAttribute(
            entryForm
        )
    }
    
    ///////////////////////////////////////////////////////////////////////////
    /**
     * post the entry.
     * expected Ajax HTTP request is '/entry/post.html'
     */
    @RequestMapping(
        value=Array("/entry/post.html"),
        method=Array(RequestMethod.POST),
        headers=Array("Accept=application/json")
    )
    @ResponseBody
    def doPost(
        @RequestBody
        entryForm: EntryForm,
        model: Model
    )
    : AjaxResponse = {
        LOG.info("called")
        
        // the response-object will be returned to the html page.
        
        if (entryForm.getTitle.equals("") || entryForm.getContent.equals("")) {
            throw new Exception("the title and content is must be set.")
        }
        
        // create a response-object.
        val response: AjaxResponse = context.getBean(
            classOf[AjaxResponse]
        )
            
        // get the mapped dto-object using the form-object data.
        val entryDto: EntryDto = getMappedEntryDto(
            entryForm
        )
            
        // post the entry dto-object using the service-object.
        postEntry(
            entryDto
        )
            
        // get the list of dto-object from the service-object.
        val entryDtoList: List[EntryDto] = getEntryDtoList(
            entryForm
        )
            
        // add to the response-object.
        addToResponse(
            entryDtoList,
            response
        )
            
        // return the response-object to html page. 
        // this will be converted into json.
        return response
    }

    ///////////////////////////////////////////////////////////////////////////
    /**
     * get the entry list.
     * expected Ajax HTTP request is '/entry/list.html'
     */
    @RequestMapping(
        value=Array("/entry/list.html"),
        method=Array(RequestMethod.POST),
        headers=Array("Accept=application/json")
    )
    @ResponseBody
    def doList(
        @RequestBody
        entryForm: EntryForm,
        model: Model
    )
    : AjaxResponse = {
        LOG.info("called")
        
        // the response-object will be returned to the html page.
        
        // create a response-object.
        val response: AjaxResponse = context.getBean(
            classOf[AjaxResponse]
        )
            
        // get the mapped dto-object using the form-object data.
        val entryDtoList: List[EntryDto] = getEntryDtoList(
            entryForm
        )
            
        // add to the response-object.
        addToResponse(
            entryDtoList,
            response
        )
            
        // return the response-object to html page.
        // this will be converted into json.
        return response;
    }
    
    ///////////////////////////////////////////////////////////////////////////
    /**
     * store the configuration data to the cookie.
     * expected Ajax HTTP request is '/entry/setting.html'
     */
    @RequestMapping(
        value=Array("/entry/setting.html"),
        method=Array(RequestMethod.POST),
        headers=Array("Accept=application/json")
    )
    def doSetting(
        @RequestBody
        entryForm: EntryForm ,
        response: HttpServletResponse 
    )
    : String = {
        LOG.info("called")

        // store the setting param to the cookie.
        storeToCookie(
            entryForm,
            response,
            604800
        )

        // redirect the request to the 'entry/form' page.
        return "redirect:/entry/form.html"
    }
    
    ///////////////////////////////////////////////////////////////////////////
    /**
     * if an error is occured, this method will be called.
     */
    @ExceptionHandler
    @ResponseBody
    def handleException(
        e: Exception
    )
    : AjaxResponse = {
        LOG.info("called")
        
        LOG.error(e.getMessage())
        
        // create a response-object.
        val response: AjaxResponse = context.getBean(
            classOf[AjaxResponse]
        )
        
        // notify the occurrence of errors to the html page.
        response.setIsError(true)
        return response;
    } 
    
    ///////////////////////////////////////////////////////////////////////////
    // private methods
    
    ///////////////////////////////////////////////////////////////////////////
    /**
     * get the dto-object of the entry using the form-object.
     */
    private def getMappedEntryDto(
        entryForm: EntryForm
    )
    : EntryDto = {
        LOG.debug("called");
        
        // create a dto-object.
        val entryDto: EntryDto = context.getBean(
            classOf[EntryDto]
        )
        
        // map the form-object to the dto-object.
        mapper.map(
            entryForm,
            entryDto
        )
        
        return entryDto
    }

    ///////////////////////////////////////////////////////////////////////////
    /**
     * post the dto-object of the entry using the service-object.
     */
    private def postEntry(
        entryDto: EntryDto
    ) = {
        LOG.debug("called");
        
        // save the dto-object.
        entryService.saveEntry(
            entryDto
        )
    }

    ///////////////////////////////////////////////////////////////////////////
    /**
     * get the list of dto-obgect from the service-object.
     */
    private def getEntryDtoList(
        entryForm: EntryForm
    )
    : List[EntryDto] = {
        LOG.debug("called");
        
        // get the dto-object list from service-object.
        val entryList: List[EntryDto] = entryService.findAllEntry(
            ""
        )
        
        return entryList
    }

    ///////////////////////////////////////////////////////////////////////////
    /**
     * add to the response-object.
     */
    private def addToResponse(
        entryDtoList: List[EntryDto],
        response: AjaxResponse
    ) = {
        LOG.debug("called");
        
        // create a list of entry object, 
        // in order to send to the html page.
        val entryModelList: List[EntryModel] = new ArrayList[EntryModel]()
                
        // process the entry object of all of the list.
        entryDtoList.foreach((entryDto: EntryDto) => {
            
            // create a object to send to the html page.
            val entryModel: EntryModel = context.getBean(
                classOf[EntryModel]
            )
            
            // set the value to the object.
            entryModel.setTitle(
                entryDto.getTitle()
            )
            entryModel.setContent(
                entryDto.getContent()
            )
            
            // add the object to the object list.
            entryModelList.add(
                entryModel
            )
        })
        
        // set the object list to response-object.
        response.setEntryModelList(
            entryModelList
        )
        
        // set the error status.
        response.setIsError(
            false
        )
    }
    
    ///////////////////////////////////////////////////////////////////////////
    /**
     * store the configuration data to the cookie.
     */
    private def storeToCookie(
        entryForm: EntryForm,
        response: HttpServletResponse,
        maxAge: Int 
    ) = {
        LOG.debug("called");
        
        val username = new Cookie(
            "__exmp_blog_username", entryForm.getUsername()
        )
        username.setMaxAge(maxAge)
        response.addCookie(username)
        
        val password = new Cookie(
            "__exmp_blog_password", entryForm.getPassword()
        )
        password.setMaxAge(maxAge)
        response.addCookie(password)
        
        val author = new Cookie(
            "__exmp_blog_author", entryForm.getAuthor()
        )
        author.setMaxAge(maxAge)
        response.addCookie(author)
    }
    
}
