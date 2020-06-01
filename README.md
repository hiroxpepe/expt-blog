# blog-examproject

---
## What's this?
a based project of a blog type application.
#### by using  
- [Spring Framework 5.2.x](https://spring.io/projects/spring-framework)
- [Spring Data JPA 2.3.x](https://spring.io/projects/spring-data-jpa)
- [Hibernate 5.4.x](http://hibernate.org/)
- [jackson 2.11.0](https://github.com/FasterXML/jackson)
- [Apache Tiles 3.0.x](https://tiles.apache.org/)
- [Lombok 1.18.x](https://projectlombok.org/)
- [Logback 1.2.x](http://logback.qos.ch/)
- [Java 8](https://www.oracle.com/)

---
## How to run the application on your local?
- get [Apache Maven](http://maven.apache.org/).
- build this project by using command line.
```bash
> cd { path to this README.md directory. }
> mvn compile
> mvn install
```
- move to the directory of webapp.
```bash
> cd webapp
```
- run the web application by using jetty plugin.
```bash
> mvn jetty:run
```
- can access to http://localhost:8080/ on your web browser.  
to stop the application hit ctrl + c

---
## Repository on Github.
[hiroxpepe/blog-examproject](https://github.com/hiroxpepe/blog-examproject)

---
## Running on Heroku.
http://blog-examproject.herokuapp.com/

---
## Author
[hiroxpepe](mailto:hiroxpepe@gmail.com)

---
## License
The examproject is released under version 2.0 of the
[Apache License](http://www.apache.org/licenses/LICENSE-2.0).
