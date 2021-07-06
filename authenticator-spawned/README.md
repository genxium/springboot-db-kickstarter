Useful tutorials in "SpringSecurity" 
- [Annotation @RolesAllowed](https://docs.oracle.com/javaee/7/api/javax/annotation/security/RolesAllowed.html), it's not "Spring specific" but instead only bound to [JSR250](https://jcp.org/aboutJava/communityprocess/final/jsr250/index.html)
- [Annotation @Secured](https://docs.spring.io/autorepo/docs/spring-security/3.2.2.RELEASE/apidocs/org/springframework/security/access/annotation/Secured.html), it's "Spring specific"
- [Building an "AuthenticationManager" by "DatabaseRecordLookup"](https://dzone.com/articles/bounty-how-to-configure-passwords-with-spring-secu#).

The use of "SpringSecurity" mainly impacts the design for persistent tables `player` and `player_login_cache`, i.e. constraining what columns they must have to be processed by some built-in methods -- though the built-in behaviours can be all overwritten but the purpose of using a framework is to respect it as much as possible.
