# three-layer-architecture

TODO intro

### Run the application locally
TODO
Pre-requisite: a Docker environment has to be available on the machine (https://docs.docker.com/get-docker/).

### Endpoints
Sandbox:  
   - API: http://localhost:8080/api/v1/colors  
   - Swagger: http://localhost:9090/actuator/swagger-ui  
   - Spring boot actuator: http://localhost:9090/actuator/health  

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
All tests classes of a given module are running in parallel (we do not execute methods in parallel in order to optimize resources instanciation only once per class as it takes quite some time)