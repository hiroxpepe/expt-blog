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

package org.examproject.blog.util;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import org.examproject.blog.dto.EntryDto;
import org.examproject.blog.entity.User;
import org.examproject.blog.repository.UserRepository;

/**
 * @author h.adachi
 */
@Component
public class UserUtils {

    private Logger LOG = LoggerFactory.getLogger(UserUtils.class);

    @Inject
    private ApplicationContext context = null;

    @Inject
    private UserRepository userRepository = null;

    ///////////////////////////////////////////////////////////////////////////
    // public methods

    public User getUser(
        EntryDto entryDto
    ){
        try {
            User user = userRepository.findByUsername(entryDto.getUsername());
            if (user == null) {
                User newUser = context.getBean(User.class);
                newUser.setUsername(entryDto.getUsername());
                newUser.setPassword(entryDto.getPassword());
                newUser.setEmail(entryDto.getEmail());
                userRepository.save(newUser);
                LOG.debug("create the new user.");
                return newUser;
            }
            return user;
        } catch (Exception e) {
            throw new RuntimeException("an error occurred.", e);
        }
    }

}
