# Authentication and Authorization Project

This project is designed to facilitate a fitness club to keep records and store them in a database further facilitating easy access to the personnel.

Used technologies: Thymeleaf + Spring Boot + Spring Security + Spring Jpa

There are three roles:
- ADMIN
- TRAINER
- USER


- [UserController](src/main/java/com/gusev/project/controller/UserController.java)
  : `/users`
    - GET/POST requests to get or change sensitive information must be available to admin only,
    - GET requests to limited information must be available to trainers
- [TrainerController](src/main/java/com/gusev/project/controller/TrainerController.java)
  : `/trainers`
    - GET/POST requests to get or change sensitive information must be available to admin only,
    - GET requests to limited information must be available to trainers and users
- [GroupClassController](src/main/java/com/gusev/project/controller/GroupClassController.java)
  : `/group-classes`
    - GET/POST requests to get or change sensitive information must be available to admin only,
    - GET requests to limited information must be available to trainers and users

## Running application locally
Application built using [Maven](https://spring.io/guides/gs/maven/). You can build a jar file and run it from the command line:
```
git clone https://github.com/AlekseyGusev/Authentication-and-Authorization-Project.git
cd Authentication-and-Authorization-Project
mvn package
java -jar target/Authentication-and-Authorization-Project-1.0-SNAPSHOT.jar
```
Or you can run it from Maven directly using the Spring Boot Maven plugin.
```
mvn spring-boot:run
```
You can access application here: http://localhost:8080/
