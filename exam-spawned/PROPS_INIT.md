# 1.定义 
Spring的`property xxx`特指context.getEnvironment().getProperty('xxx')，并且可以用下列方式获取到值
- `@Value(${xxx})`

对于一个使用了SpringBoot(注意不是仅Spring，但以下初始化步骤大部分也适用于仅Spring)单ApplicationContext的JVM进程，其初始化步骤为o
- a. SpringBoot `static main` 函数运行 
- b. 进程中实例化ApplicationContext 
- c. ApplicationContext获取当前activeProfile变量的值，比如default, staging或production 
- d. 读取`application.properties`或`application.yml`成<k, v> pair进context.getEnvironment().properties  
- e. 读取`application-<activeProfile>.yml`成<k, v> pair进context.getEnvironment().properties
- f. 进行beanFactory阶段，实例化各个bean

# 2.从Zookeeper获取property应该嵌入的位置
在很多应用场景下，我们可能希望可以从
- `application.properties`或`application.yml`
- `application-<activeProfile>.yml`
以外的提供者获取properties，比如一个nacos或者zookeeper服务。

这种情况下，我们有两个选择
- 在`1.a`和`1.b`之间，连接nacos或者zookeeper获取信息进一个`Map<String, String> extProps`对象，然后用`new SpringApplicationBuilder(MyApp.class).properties(extProps).run(args)`运行, 或者
- 在`1.f`最早的阶段使用一个`ExtPropsInjector bean`连接nacos或者zookeeper获取信息，并写入到context.getEnvironment().properties， 比如使所有其他自己写的beans在构造函数阶段都依赖`@Autowired ExtPropsInjector`。 

**提倡使用第一种方法，因为这样有冲突的property key就会以`application.properties`或`application.yml`中的为准**。

但是这个项目中shared-module的ZkPropsConfigs用的是第二种方法。

