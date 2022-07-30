# About PB codegen
Don't use maven java-grpc codegen plugins, they're buggy.

# Using external classpath
Launch with 
```aidl
shell> java -D"loader.path=/path/to/classes" -jar /path/to/grpc-ha-spawned-<version>.jar
```
to add an extra `/path/to/classes` to classpath.