Tupl Support
============

# Overview

This library intended to provide a set of support interfaces with default implementations as
well as set of DAO exceptions, specific to Tupl operations.

These interfaces can be inherited by certain DAO (data access object) in order to provide easier access to Tupl
operations, such as accessing the indexes or operating with transactions.

This library includes the following modules:

* ``tupl-test-support`` - utility methods for Tupl integration tests.
* ``tupl-support`` - helpers for creating Tupl data access objects.
* ``tupl-protobuf-support`` - extension of ``tupl-support`` providing helpers, that combine protocol buffers support with ``tupl-support``.

## Key entities

* ``TuplTransactionManager`` and its descendant ``StandardTuplTransactionManager`` which is a central interface, around which every DAO revolves.
This interface provides a contract for accessing Tupl transactions which is then used by all extension interfaces, provided in ``tupl-support``.
* ``IdSupport`` - helper interface with default methods that provides a way to create an ID and convert that ID to and from byte array Tupl keys
* ``DaoException`` and its descendants - provides a common set of exceptions, thrown from Tupl DAOs.
* ``TuplLoadSupport`` and ``ByteArrayResultMapper`` - base entities that provide support for uniform data retrieval from Tupl databases.
* ``TuplUpdateSupport`` - helper interface with default methods that provide support for insert, update and delete operations.
* ``PersistenceMapDao`` - a contract, that exposes access to Tupl index in a form of a Map-alike interface, these methods include get, put and enumerate operations.
** ``PersistenceMapDao.newStringMap`` - String-to-String implementation of ``PersistenceMapDao``
** ``StandardPersistentMapDao`` - standard abstract base for ``PersistenceMapDao``.
** ``ProtobufModelPersistentMapDao`` from ``tupl-protobuf-support`` - support for protocol buffer messages DAO.

## Samples

See sample DAO implementation in [BookDaoImpl](https://github.com/truward/tupl-support/blob/master/tupl-support/src/test/java/com/truward/tupl/support/testDao/BookDaoImpl.java)

See persistent map tests in:

* [PersistentMapDaoTest](https://github.com/truward/tupl-support/blob/master/tupl-support/src/test/java/com/truward/tupl/support/map/PersistentMapDaoTest.java)
* [PersistentMapDaoTest](https://github.com/truward/tupl-support/blob/master/tupl-protobuf-support/src/test/java/com/truward/tupl/protobuf/ProtobufModelPersistentMapDaoTest.java)

# How to use

Add jar dependency in your pom.xml:

```xml
<dependency>
  <groupId>com.truward.tupl</groupId>
  <artifactId>tupl-support</artifactId>
  <version>1.0.1</version>
</dependency>
```
