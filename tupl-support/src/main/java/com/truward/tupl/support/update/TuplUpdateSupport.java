package com.truward.tupl.support.update;

import com.truward.tupl.support.exception.KeyNotFoundException;
import com.truward.tupl.support.id.Key;
import com.truward.tupl.support.index.TuplIndexSupport;
import com.truward.tupl.support.transaction.TuplTransactionSupport;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Mixin that provides support for insert operation for Tupl DB.
 *
 * @author Alexander Shabanov
 */
public interface TuplUpdateSupport extends TuplTransactionSupport, TuplIndexSupport {

  /**
   * Inserts or replaces an object in the database, depending on whether the
   * passed ID is null or not.
   *
   * @param indexName Index name
   * @param id ID of the object to be updated. If null - a new entry will be created and generated ID will be returned
   * @param objectContents Object contents, to be associated with a given or newly created ID
   * @return ID of the updated object. Matches given ID if passed ID is null
   */
  @Nonnull
  default Key updateObject(@Nonnull String indexName,
                           @Nullable Key id,
                           @Nonnull byte[] objectContents) {
    return withTransaction(tx -> withIndex(indexName, index -> {
      Key resultId = id;
      if (resultId != null) {
        if (!index.replace(tx, resultId.getBytesNoCopy(), objectContents)) {
          throw new KeyNotFoundException(id);
        }
      } else {
        boolean inserted;
        do {
          resultId = Key.random();
          inserted = index.insert(tx, resultId.getBytesNoCopy(), objectContents);
        } while (!inserted); // repeat until new key is created
      }
      return resultId;
    }));
  }

  /**
   * Unconditionally associates an object with a specified key.
   *
   * @param indexName Index name
   * @param id ID of the object to be updated
   * @param objectContents Object contents, to be associated with a given ID
   */
  default void storeObject(@Nonnull String indexName,
                           @Nonnull Key id,
                           @Nonnull byte[] objectContents) {
    withTransaction(tx -> withIndex(indexName, index -> {
      index.store(tx, id.getBytesNoCopy(), objectContents);
      return null;
    }));
  }

  /**
   * Unconditionally deletes an object associated with a specified key.
   *
   * @param indexName Index name
   * @param id ID of the object to be deleted
   * @return True, if object with a specified key has been found
   */
  default boolean deleteObject(@Nonnull String indexName, @Nonnull Key id) {
    return withTransaction(tx -> withIndex(indexName, index -> index.delete(tx, id.getBytesNoCopy())));
  }
}
