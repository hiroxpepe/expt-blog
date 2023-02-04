# expt-blog

---
## What's this?
A base project of a blog-type web application.

#### by using  
- [Spring Framework 5.3.x](https://spring.io/projects/spring-framework)
- [Spring Data JPA 2.7.x](https://spring.io/projects/spring-data-jpa)
- [Hibernate 5.6.x](http://hibernate.org/)
- [jackson 2.13.0](https://github.com/FasterXML/jackson)
- [Apache Tiles 3.0.x](https://tiles.apache.org/)
- [Lombok 1.18.x](https://projectlombok.org/)
- [Logback 1.2.x](http://logback.qos.ch/)
- [Java 11](https://openjdk.org/)

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
[hiroxpepe/expt-blog](https://github.com/hiroxpepe/expt-blog)

---
## Author
[hiroxpepe](mailto:hiroxpepe@gmail.com)

---
## License
The expt is released under version 2.0 of the
[Apache License](http://www.apache.org/licenses/LICENSE-2.0).
