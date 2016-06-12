Tupl Support
============

# Overview

This library intended to provide a set of support interfaces with default implementations as
well as set of DAO exceptions, specific to Tupl operations.

These interfaces can be inherited by certain DAO in order to provide easier access to Tupl
operations, such as accessing the indexes or operating with transactions.

This library includes the following modules:

* ``tupl-test-support``
* ``tupl-support``
* ``tupl-protobuf-support``

See also sample DAO implementation in [BookDaoImpl](https://github.com/truward/tupl-support/blob/master/tupl-support/src/test/java/com/truward/tupl/support/testDao/BookDaoImpl.java).

# How to use

Add jar dependency in your pom.xml:

```xml
<dependency>
  <groupId>com.truward.tupl</groupId>
  <artifactId>tupl-support</artifactId>
  <version>1.0.1</version>
</dependency>
```
