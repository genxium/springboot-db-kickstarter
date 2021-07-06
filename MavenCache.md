It's possible that during development we deployed some jars with a `*-SNAPSHOT` version to an upstream repository, then at some point of time found a failure of overwriting the deployed jars with the same `*-SNAPSHOT` version. In this case the following one-liner might be useful to clean up the local mvn cache.
```
mvn dependency:purge-local-repository -DactTransitively=false -DreResolve=false
```
