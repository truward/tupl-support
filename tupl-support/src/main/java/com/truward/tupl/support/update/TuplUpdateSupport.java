package com.truward.tupl.support.update;

import com.truward.tupl.support.exception.KeyNotFoundException;
import com.truward.tupl.support.id.IdOperations;
import com.truward.tupl.support.index.TuplIndexSupport;
import org.cojen.tupl.Transaction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Mixin that provides support for insert operation for Tupl DB.
 *
 * @author Alexander Shabanov
 */
public interface TuplUpdateSupport extends TuplIndexSupport, IdOperations {

  /**
   * Inserts or replaces an object in the database, depending on whether the
   * passed ID is null or not.
   *
   * @param tx Transaction, to perform this operation in
   * @param indexName Index name
   * @param id ID of the object to be updated. If null - a new entry will be created and generated ID will be returned
   * @param objectContents Object contents, to be associated with a given or newly created ID
   * @return ID of the updated object. Matches given ID if passed ID is null
   */
  @Nonnull
  default String updateObject(@Nonnull Transaction tx,
                              @Nonnull String indexName,
                              @Nullable String id,
                              @Nonnull byte[] objectContents) {
    return withIndex(indexName, index -> {
      String resultId = id;
      if (resultId != null) {
        if (!index.replace(tx, fromId(resultId), objectContents)) {
          throw new KeyNotFoundException(id);
        }
      } else {
        boolean inserted;
        do {
          resultId = generateId();
          inserted = index.insert(tx, fromId(resultId), objectContents);
        } while (!inserted); // repeat until new key is created
      }
      return resultId;
    });
  }

  /**
   * Unconditionally associates an object with a specified key.
   *
   * @param tx Transaction, to perform this operation in
   * @param indexName Index name
   * @param id ID of the object to be updated
   * @param objectContents Object contents, to be associated with a given ID
   */
  default void storeObject(@Nonnull Transaction tx,
                           @Nonnull String indexName,
                           @Nonnull String id,
                           @Nonnull byte[] objectContents) {
    withIndex(indexName, index -> {
      index.store(tx, fromId(id), objectContents);
      return null;
    });
  }

  /**
   * Unconditionally deletes an object associated with a specified key.
   *
   * @param tx Transaction, to perform this operation in
   * @param indexName Index name
   * @param id ID of the object to be deleted
   * @return True, if object with a specified key has been found
   */
  default boolean deleteObject(@Nonnull Transaction tx,
                            @Nonnull String indexName,
                            @Nonnull String id) {
    return withIndex(indexName, index -> index.delete(tx, fromId(id)));
  }
}
