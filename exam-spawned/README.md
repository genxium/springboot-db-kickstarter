# Compiling and packaging
This application depends on the `shared-module`, thus need compiling and installing the `shared-module` locally first.
```
proj-root/shared-module> mvn -U clean install 
proj-root/exam-spawned> mvn -U -DskipTests clean package 
```

If you don't need cleaning all cached maven deps in `$HOME/.m2`, omit the `-U` option.

# Running
Foreground
```
proj-root/exam-spawned> java -jar target/exam-spawned*.jar  
```

Background
```
proj-root/exam-spawned> java -jar target/exam-spawned.jar > /dev/null 2>&1 &  
```
Don't mind the depletion of console logs, we'll configure logging into file by `logback.xml`.
