How to release
==============

```
mvn release:clean release:prepare -P release
```

Then

```
mvn release:perform -P release
```
