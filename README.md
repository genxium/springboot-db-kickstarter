SpringCloud will be used in this project to manage a "multi-process-cluster", and the concrete "registry" used would be "NetflixEureka", see [this spring-cloud-netflix doc](https://docs.spring.io/spring-cloud-netflix/docs/2.2.5.RELEASE/reference/html/) for more information.

The "registry" should be able to serve the following purposes.
- Registering and discovering of different "Services(usually different `LinuxProcess`es)" for each other.
- Registering and discovering of different "rpc(ip:port endpoint + method signature)" for each other.
- [Optional] Holding shared configurations/properties.

The "rpc protocol" in use would be "Feign", checkout https://dzone.com/articles/microservices-in-spring-eureka for a getting-started example.

Kindly note that the "registry" is NOT NECESSARILY responsible to serve as a cache for "rate limiting libs, e.g. [bucket4j](https://github.com/vladimir-bukhtoyarov/bucket4j)" or as a "message broker, e.g. [redis](https://redis.io/)".
