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

package org.examproject.blog.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author h.adachi
 */
@Data
@ToString(exclude={"category","user"})
@Entity
@Table(name="entries")
@Component
@Scope(value="prototype")
public class Entry implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(unique=true)
    Long id;

    @Column(name="code", unique=true, length=32)
    String code;

    @Column(name="created")
    @Temporal(TemporalType.TIMESTAMP)
    Date created;

    @Column(name="updated")
    @Temporal(TemporalType.TIMESTAMP)
    Date updated;

    @Column(name="title", length=48)
    String title;

    @Column(name="content", length=2048)
    String content;

    @ManyToOne
    Category category;

    @ManyToOne
    User user;

//    @OneToMany(mappedBy="entry", fetch=FetchType.EAGER)
//    Set<EntryTag> entryTagSet = new HashSet<>();

}
