No "SpringBootParent/SpringBootStarter" or "SpringCloud" dependency is allowed in this module to avoid confusion.

No "connection endpoint/credentials" config file should be put in this module, it's meant to be a dependency of modules that connect to different database endpoints (prefixed by "spring.rwds").

It's good enough to have a single "BeanExporter" instance, and thus a single "RwPoolRoutingDataSource" even when running in multi-threaded environment. **However**, one of the major concerns of this "shared-module" is that it exported a bean "DualAuthenticationFilterChainProxy" for others to use, all of 
- DualAuthenticationFilterChainProxy
- DatabaseCredentialsAuthenticationFilter
- DatabaseRememberMeServices
- DatabaseAuthenticationManager

should have more than 1 instances for running in a multi-threaded environment for the sake of efficiency, **otherwise being "bean"s with many "MemberVariable"s they MIGHT BE PRONE TO thread switch errors**. By far our rule of thumb is that `except for calling SecurityContextHolder.getContext().setAuthentication(...) and beanExporter.getJdbcTemplate().Xxx(...), all other methods of all these bean-classes should be stateless`, where `SecurityContextHolder.getContext().setAuthentication(...) and beanExporter.getJdbcTemplate().Xxx(...)` are already magically kept as "thread-safe anchors".