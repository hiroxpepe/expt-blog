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

package org.examproject.blog.entity

import java.lang.Long
import java.io.Serializable
import java.util.HashSet
import java.util.Set
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

import scala.SerialVersionUID
import scala.reflect.BeanProperty

/**
 * @author hiroxpepe
 */
@Entity
@Table(name="users")
@Component
@Scope(value="prototype")
@SerialVersionUID(-8712872385957386182L)
class User extends Serializable {
  
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(unique=true)
    @BeanProperty
    var id: Long = _
    
    @Column(name="username", unique=true, length=16)
    @BeanProperty
    var username: String = _
    
    @Column(name="password", length=16)
    @BeanProperty
    var password: String = _
    
    @Column(name="enable")
    @BeanProperty
    var enable: Boolean = true
    
    @OneToMany(mappedBy="user", fetch=FetchType.EAGER)
    @BeanProperty
    var entrySet: Set[Entry] = new HashSet[Entry]()
    
    @OneToMany(mappedBy="user", fetch=FetchType.EAGER)
    @BeanProperty
    var groupSet: Set[Group] = new HashSet[Group]()
    
    @OneToMany(mappedBy="user", fetch=FetchType.EAGER)
    @BeanProperty
    var interestSet: Set[Interest] = new HashSet[Interest]()
    
}