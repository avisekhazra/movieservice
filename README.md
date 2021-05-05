# Movie Trailer Service

Instructions to deploy the solution to local,

1. git clone https://github.com/avisekhazra/movieservice.git
2. cd movieservice
3. docker compose up --build -d

## System requirements

- Java 11
- Docker compose 3.8
- Docker Engine version **19.03.0** and higher
- Maven 3.3.9 +

## Application Overview

#### Current Architecture
![image](https://user-images.githubusercontent.com/19636598/117209519-ba0e6e00-adf6-11eb-88c1-c95f80e2931b.png)





### Tenets:

Sprint boot application is created to set up the service. 

#### Caching: 

Multi layered caching has been implemented.

On Application level, Redis is used to cache the intermediate result. Redis is chosen because Memcached is not supported in Spring boot out of the box and also Redis is more suitable for our use case.

Edge Optimized API gateway has been deployed which is in combination of the Cloudfront(CDN).

API GW cache has been enabled with a TTL of 300s.

#### Security:

API endpoint is private and requires OAuth token to access the endpoint. OAuth client_credentials flow has been implemented which means that the API should be integrated with BE service.  Authorization is offloaded to API GW.

API GW is configured with API keys which helps to implement some of the DDoS mitigation mechanisms as by throttling the requests.

Application layer could be further secured by proposed private network in the desired architecture.

#### Scalability

AWS API GW , Cognito , EC2 on demand instance have been used to deploy the solution. AWS API GW and Cognito being managed service are fully scalable.

Application layer could be further scalable as mentioned in the desired architecture + CDN.

#### Technical details

The API requires query, country, language, page(optional) request parameters. Currently only language 'en' is supported. Country and language only supports valid ISO codes.

Pagination has been implemented in the service. Default page size is 10.

Multithreading has been used to do parallel processing.

Few Spring concept has been used, Controller, ControllerAdvice for validating the input as well as custom error handling, ConfigurationProperties to bootstrap the configurations.

#### Unit testing

Unit testing has been implemented with Junit5. 

Few Extensions/library used - Mockito, BDD Mockito, AssertJ, MockMvc etc.

Unit tests have been implemented for Services and the controller with more than 94% coverage.

To run the unit test cases,

- start redis locally - docker run --name my-first-redis -p 6379:6379 -d redis
- navigate to the folder where pom.xml is there and run ->  mvn test -Ptest

Test coverage report is located at -  ~ /target/site/jacoco/index.html



#### Others:

Application is dockerized with two containers - Application container and Redis container as Sidecar pattern (docker compose).


## Improvements

- Monitoring to be implemented.
- Unit test coverage to be improved.
- Multi lingual support for the API.
- Auto suggest for the search.
- Integration test cases.
- Infrastruce as code.
#### Desired architecture
![image](https://user-images.githubusercontent.com/19636598/117210857-86344800-adf8-11eb-9ade-d34a8feda645.png)





