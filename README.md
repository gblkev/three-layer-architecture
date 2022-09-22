# three-layer-architecture

This application is a complete implementation (with real tests!) of a 3-layer architecture based on the following technologies:
   - Java
   - Spring boot
   - JUnit 5 + Testcontainers + MockServer
   - MySQL, Redis, Kafka

Here are the implemented features:
   - An API (a v1 + a v2) with its Swagger
   - A scheduled task
   - Consumption of a Kafka queue
   - Exposition of a JMX resource

### Run the application locally
Pre-requisite: in order for testcontainers to work, a Docker server has to be available on the machine (https://docs.docker.com/get-docker/).  
Build modules in parallel (1 thread per available CPU core):
   - mvn -T 0.5C clean install

Takes 4min on my 7-year old pc with the following configuration in my ${HOME}\.wslconfig :
```
[wsl2]
memory=8GB
processors=4
```
With a decent pc, it should be much much faster.

TODO To add a message in Kafka locally, do....

### Architecture
TODO
sandbox: provides components necessary to run the application (mysql db, redis, kafka, rest mocks).
It's used in 2 different contexts:
   - It lets you run the application locally, against a "real" environment (with fixed ports)
   - It provides those same components to the integration tests (with random ports)

three-layer-architecture-sandbox starts components needed to run the application locally (database, cache, mock of external APIs, etc.). Just run the class SandboxApplication.  
Pre-requisite: a Docker environment has to be available on the machine (https://docs.docker.com/get-docker/).


### Tests
dao -> only IT because UT do not make any sense
business -> UT (no IT because partially covered by e2e-tests)
controller -> only IT because UT do not make any sense
All containers necessary to run the application (MySQL, Redis, Kafka) are started with random ports once for every Maven module.
Then, data are cleared after each test / method.

### Endpoints
Sandbox:  
   - API: http://localhost:8080/api/v1/colors  
   - Swagger: http://localhost:9090/actuator/swagger-ui  
   - Spring boot actuator: http://localhost:9090/actuator/health  
jolokia (JMX over HTTP)  http://localhost:9090/actuator/jolokia/read/java.lang:type=Memory/HeapMemoryUsage