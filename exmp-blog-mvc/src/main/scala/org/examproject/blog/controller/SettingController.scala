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

import javax.inject.Inject
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.dozer.Mapper
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody

import org.examproject.blog.dto.EntryDto
import org.examproject.blog.form.EntryForm
import org.examproject.blog.service.EntryService
import org.examproject.blog.response.AjaxResponse

import scala.collection.JavaConversions._

/**
 * the entry controller class of the application.
 *
 * @author hiroxpepe
 */
@Controller
class SettingController {
    
    private val LOG: Logger = LoggerFactory.getLogger(
        classOf[SettingController]
    )
    
    @Inject
    private val context: ApplicationContext = null

    @Inject
    private val request: HttpServletRequest = null
    
    @Inject
    private val mapper: Mapper = null

    @Inject
    private val entryService: EntryService = null
    
    ///////////////////////////////////////////////////////////////////////////
    // public methods
     
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
