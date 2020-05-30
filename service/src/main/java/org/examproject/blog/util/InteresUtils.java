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

import java.util.Set;
import java.util.HashSet;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import org.examproject.blog.dto.EntryDto;
import org.examproject.blog.entity.Interest;
import org.examproject.blog.entity.User;
import org.examproject.blog.repository.EntryRepository;
import org.examproject.blog.repository.InterestRepository;

/**
 * @author h.adachi
 */
@Component
public class InteresUtils {

    private Logger LOG = LoggerFactory.getLogger(InteresUtils.class);

    @Inject
    private ApplicationContext context = null;

    @Inject
    private EntryRepository entryRepository = null;

    @Inject
    private InterestRepository interestRepository = null;

    @Inject
    private CategoryUtils categoryUtils = null;

    @Inject
    private TagUtils tagUtils = null;

    @Inject
    private UserUtils userUtils = null;

    ///////////////////////////////////////////////////////////////////////////
    // public methods

    public Interest getDefaultInterest(
        EntryDto entryDto
    ){
        try {
            User user = userUtils.getUser(entryDto);
            Interest interest = interestRepository.findByUserAndName(
                user,
                entryDto.getUsername()
            );
            if (interest == null) {
                Interest newInterest = context.getBean(Interest.class);
                newInterest.setUser(user);
                newInterest.setName(entryDto.getUsername());
                interestRepository.save(interest);
                return newInterest;
            }
            return interest;
        } catch (Exception e) {
            throw new RuntimeException("an error occurred.", e);
        }
    }

    public Set<Interest> getDefaultInterestSet(
        EntryDto entryDto
    ){
        try {
            Interest interest = getDefaultInterest(entryDto);
            Set<Interest> interestSet = new HashSet<>();
            interestSet.add(interest);
            return interestSet;
        } catch (Exception e) {
            throw new RuntimeException("an error occurred.", e);
        }
    }

}
