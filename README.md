# Car Seller example Rest API



[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

Minimal [Spring Boot](http://projects.spring.io/spring-boot/) sample app.

## Requirements

For building and running the application you need:

- [JDK 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- [Gradle](https://gradle.org/install/)

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.ca.agency.car.seller.CarSellerApplication` class from your IDE.

Alternatively, you can use the [Spring Boot Gradle plugin](https://spring.io/guides/gs/gradle/) like so:

```shell
./gradlew bootRun
```
By default, the application will starts in port 8080.

Additionally you can specify your preferred port as the following:

```shell
./gradlew bootRun --args='--server.port=8888'
```

For more Information about this Rest API you can check on the [live documentation](https://carseller.readme.io/reference/listdealersusingget?useReact=on) or you can access a live demo after running the application in the swagger path `http://[port]/swagger-ui/index.html` 

## Additional functionality

You will find some extra features in the API which I believe will be of great value to the service, like the Question API, which enables the possibility to ask more information about the Listing and gives the ability to the Dealers to Answer these Questions.

More so, to gain some insights, I registered all the unpublishing events in the DataBase so the client can understand better why a Listing was unpublish.

## Unit Testing

This project uses [Jacoco Gradle Plugin](https://docs.gradle.org/current/userguide/jacoco_plugin.html) for test coverage. You can generate the Test Coverage report by executing:

```shell
./gradlew clean test jacocoTestReport
```
The resulted test coverage can be found at `build/reports/jacoco/test/html/index.html` 

Below is the latest Jacoco Test Report:
![image](https://user-images.githubusercontent.com/17863794/121129147-20b9e980-c7f2-11eb-831a-d5616be73cac.png)


## Copyright

Released under the Apache License 2.0.
