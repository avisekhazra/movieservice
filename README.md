# Movie Trailer Service

Sprint boot application is created to set up the service. 

Redis is used to cache the intermediate result. Redis is chosen because Memcached is not supported in Spring boot out of the box and also Redis is more suitable for our use case.

Junit + Mockito + AssertJ + MockMvc have been used to implement unit testing.

Pagination has been implemented in the service.

For now only support for English language has been built.



Instructions to deploy the solution to local,

1. git clone <repo>
2. docker compose up

## System requirements

Java 11

Docker compose 3.8

Docker Engine version **19.03.0** and higher

Maven 3.3.9 +