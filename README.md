# three-layer-architecture

### Introduction
This application is a complete implementation (with real tests!) of a 3-layer architecture based on the following technologies:
   - Java 17 + Maven wrapper
   - Spring boot
   - JUnit 5 + Testcontainers + MockServer
   - MySQL, Redis, Kafka

Here are the implemented features:
   - An API (a v1 + a v2) with its Swagger
   - A scheduled task
   - Consumption of a Kafka queue
   - Exposition of a JMX resource

The 3-layer architecture should not get mixed up with the 3-tier architecture.  
A layer is a division of a given application (so inside a unique JVM).  
A tier is a division of a system into several applications.  
Example of a 2-tier architecture: a frontend app (tier 1) running on a server A, communicating with a backend API (tier 2) running on a server B.  
In other words, a n-tier architecture happens at a higher level of architecture than a n-layer architecture.  

### Architecture
**parent**  
It's the Bill Of Materials (BOM) of the application. Any version of a library or a plugin used in this project must be defined in it.  
It also contains the plugins configuration that has to be shared between all Maven modules (Java version, lombok configuration, tests configuration, etc.).  
All Maven modules composing the project inherit from this pom.

**sandbox**  
It provides components necessary to run the application (MySQL db, Redis cache, Kafka queue, Rest mocks).  
It's used in 2 different contexts:
   - It lets you run the application locally, against a "real" environment (with fixed ports)
   - It provides those same components to the integration tests (with random ports)

The sandbox does not depend on any other Maven module (except the BOM).

**dao**  
DAO stands for Data Access Objects. It's maybe not the best term but it's probably the term which will speak to people the most.  
This layer contains every component that connects to a system external to the JVM (a web service, a database, a distributed cache, etc.) or something that handles data for the application (a local cache for instance).  

**business**  
It's the heart of the application. It contains all the logic of the application.  

**controller**  
It's the remote controller of the application. Any event that triggers an action is implemented there.  
Triggers can be varied:
   - An API
   - Consumption of a queue (Kafka, RabbitMQ, Redis stream, etc.)
   - A scheduled task
   - A JMX resource
   - etc.

![Architecture](three-layer-architecture.drawio.png?raw=true)

### Build the application
Pre-requisite: in order for testcontainers to work, a Docker server has to be available on the machine (https://docs.docker.com/get-docker/).  
Build modules in parallel (1 thread per available CPU core):
```
mvnw -T 1C clean install
```

It takes 4 minutes on my 7-year old pc with the following configuration in my ${HOME}\.wslconfig:
```
[wsl2]
memory=8GB
processors=4
```
With a decent pc, it should be much, much faster.

### Run the application locally
   - Start the sandbox: run SandboxApplication class as a Spring Boot application from the IDE.
   - Start the application: run ThreeLayerArchitectureApplication class as a Spring Boot application from the IDE.
   - To add a new message in Kafka, run the following JMX operation (with JConsole for instance): threelayerarchitecture:name=kafka.consumers, publishCreateColorMessageToKafka

### Tests
**sandbox** -> only integration tests because unit tests do not make sense  
**dao** -> only integration tests because unit tests do not make sense  
**business** -> only unit tests  
**controller** -> only integration tests  
All components necessary to run the application (MySQL, Redis, Kafka, etc.) are started with random ports once for every Maven module.  
Then, data is cleared after each test / method.  

### Endpoints
Sandbox:  
   - API: http://localhost:8080/api/v1/colors  
   - Swagger: http://localhost:9090/actuator/swagger-ui  
   - Spring boot actuator: http://localhost:9090/actuator/health  
   - Jolokia (JMX over HTTP):  http://localhost:9090/actuator/jolokia/read/java.lang:type=Memory/HeapMemoryUsage