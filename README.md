# examproject (c) by examproject

***
### What's this?
A blog based project using [Spring MVC](http://static.springsource.org/spring/docs/3.1.x/spring-framework-reference/html/mvc.html).  

***
### How to run at the local?
You will need to get [Apache Maven](http://maven.apache.org/).

You will need to build this example at your command line.

    > cd { path to this README.md directory. }
    > mvn compile
    > mvn install

Move to launcher project directory.

    > cd exmp-blog-webapp

To run the .war application by jetty plugin.

    > mvn jetty:run

You can access to http://localhost:8080/ on your web browser.  
To stop the application hit ctrl + c

***
### How to run on the Heroku?
You will need to get [Git](http://git-scm.com/), of course.  
You will need to create a application for [Heroku](http://www.heroku.com/), and must need to get the heroku tools, used [RubyGems](http://rubygems.org/).  

To push the application for Heroku at your command line.

    > cd { path to this README.md directory. }
    > heroku login
    > git push git@heroku.com:your-app-name.git

***
### Hosted on GitHub.
[hiroxpepe/exmpblog](https://github.com/hiroxpepe/exmpblog)

***
### Running on Heroku.
http://exmpblog.herokuapp.com/

***
### Author
[hiroxpepe](mailto:hiroxpepe@gmail.com)

***
### License
The examproject is released under version 2.0 of the
[Apache License](http://www.apache.org/licenses/LICENSE-2.0).
