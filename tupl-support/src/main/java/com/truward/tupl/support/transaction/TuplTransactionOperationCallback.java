package com.truward.tupl.support.transaction;

import org.cojen.tupl.Transaction;

import java.io.IOException;

/**
 * @param <T> Result of operation, performed with the given transaction.
 *
 * @author Alexander Shabanov
 */
public interface TuplTransactionOperationCallback<T> {
  T call(Transaction tx) throws IOException;
}
