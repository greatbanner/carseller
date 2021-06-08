# Car Seller example Rest API



[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

Minimal [Spring Boot](http://projects.spring.io/spring-boot/) sample app.

## Requirements

For building and running the application you need:

- [JDK 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- [Gradle](https://gradle.org/install/)

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.ca.agency.car.seller.CarSellerApplication` class from your IDE.

Alternatively you can use the [Spring Boot Gradle plugin](https://spring.io/guides/gs/gradle/) like so:

```shell
./gradlew bootRun
```
By default, the application will starts in port 8080.

Aditionaly you can specify your prefered port as the following:

```shell
./gradlew bootRun --args='--server.port=8888'
```

For more Information about this Rest API you can check on the [live documentation](https://carseller.readme.io/reference/listdealersusingget?useReact=on) or you can access a live demo after running the application in the swagger path `http://[port]/swagger-ui/index.html` 

## Additional functionality

You will find some extra features in the API which I belive will be of great value to the service, like the Question API, that enables the posibility to ask more information about the Listing and gives the hability to the Dealers to Answer this Questions.

More so, in order to gain some insights, I registered all the unpublishing events in the DataBase so the client can understand better why a Listing was unpublish.

## Unit Testing

This project uses [Jacoco Gradle Plugin](https://docs.gradle.org/current/userguide/jacoco_plugin.html) for test coverage. You can generate the Test Coverage report by executing:

```shell
./gradlew clean test jacocoTestReport
```
The resulted test coverage can be found at `build/reports/jacoco/test/html/index.html` 

Below is the latest Jacoco Test Report:
![image](https://user-images.githubusercontent.com/17863794/121126073-3082ff00-c7ed-11eb-97ff-a77d6342304b.png)


## Copyright

Released under the Apache License 2.0.
