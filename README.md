# Movie Trailer Service

Instructions to deploy the solution to local,

1. git clone <repo>
2. cd movieservice
3. docker compose up --build -d

## System requirements

Java 11

Docker compose 3.8

Docker Engine version **19.03.0** and higher

Maven 3.3.9 +

## Application Overview

#### Current Architecture

![1620057328764](C:\Users\GEBRUI~1\AppData\Local\Temp\1620057328764.png)



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

#### Scalability

AWS API GW , Cognito , EC2 on demand instance have been used to deploy the solution. AWS API GW and Cognito being managed service are fully scalable.

Application layer could be further scalable as mentioned in the desired architecture + CDN.

#### Technical details

The API requires query, country, language, page(optional) request parameters. Currently only language 'en' is supported. Country and language only supports valid ISO codes.

Pagination has been implemented in the service. Default page size is 10.

Multithreading has been used to do parallel processing.



#### Others:

Application is dockerized with two containers - Application container and Redis container as Sidecar pattern (docker compose).

Junit + Mockito + AssertJ + MockMvc have been used to implement unit testing.

Pagination has been implemented in the service.

For now only support for English language has been built.



#### Desired architecture

![1620058208643](C:\Users\GEBRUI~1\AppData\Local\Temp\1620058208643.png)



