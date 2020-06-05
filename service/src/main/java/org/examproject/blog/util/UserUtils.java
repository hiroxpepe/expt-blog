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

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import org.examproject.blog.dto.EntryDto;
import org.examproject.blog.entity.User;
import org.examproject.blog.repository.UserRepository;

/**
 * @author h.adachi
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class UserUtils {

    @NonNull
    private final ApplicationContext context;

    @NonNull
    private final UserRepository userRepository;

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
                log.debug("create the new user.");
                return newUser;
            }
            return user;
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new RuntimeException("an error occurred.", e);
        }
    }

}
