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

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.dozer.Mapper
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

import org.examproject.blog.dto.EntryDto
import org.examproject.blog.model.EntryModel
import org.examproject.blog.service.EntryService

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
     * expected HTTP request is 'http://localhost:8080/entry/nnnnnnnnn.html'
     */
    @RequestMapping(
        value=Array("/entry/{code}.html"),
        method=Array(RequestMethod.GET)
    )
    def doGet(
        @PathVariable code: String,
        model: Model
    ): String = {
        LOG.info("called")
        LOG.debug("code: " + code)
     
        // set the model-object to the model. 
        model.addAttribute(
            getEntryModel(code)
        )
        
        return "entry/permalink";
    }
    
    ///////////////////////////////////////////////////////////////////////////
    // private methods
    
    ///////////////////////////////////////////////////////////////////////////
    /**
     * get the model-object from service-object.
     */
    private def getEntryModel(
        code: String
    )
    : EntryModel = {
        LOG.debug("called");
        
        // get a dto-object from service-object.
        val entryDto: EntryDto = entryService.getEntryByCode(
            code
        )
    
        // create a model-object.
        val entryModel: EntryModel = context.getBean(
            classOf[EntryModel]
        )
    
        // map the dto-object to the model-object.
        mapper.map(
            entryDto,
            entryModel
        )
        
        return entryModel
    }
    
}
