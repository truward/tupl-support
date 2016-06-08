package com.truward.tupl.support.transaction;

import com.truward.tupl.support.TuplDatabaseProvider;
import com.truward.tupl.support.exception.InternalErrorDaoException;
import org.cojen.tupl.Transaction;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * Mixin that provides support for Tupl transactions.
 *
 * @author Alexander Shabanov
 */
public interface TuplTransactionSupport extends TuplDatabaseProvider {

  /**
   * Gets transaction manager, which is then supposed to be used within
   * {@link #withTransaction(TuplTransactionOperationCallback)} function,
   *
   * @return Transaction manager instance
   */
  @Nonnull
  TuplTransactionManager getTransactionManager();

  default <T> T withTransaction(@Nonnull TuplTransactionOperationCallback<T> callback) {
    final TuplTransactionManager txManager = getTransactionManager();
    final Transaction tx = txManager.getTransaction();
    try {
      try {
        final T result = callback.call(tx);
        txManager.commitTransaction(tx);
        return result;
      } finally {
        txManager.exitTransaction(tx);
      }
    } catch (IOException e) {
      throw new InternalErrorDaoException(e);
    }
  }
}
