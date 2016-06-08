package com.truward.tupl.support.transaction;

import org.cojen.tupl.Database;
import org.cojen.tupl.Transaction;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * An access to the class, that owns transaction manager.
 * All the DAOs that share an access to the same database should also share the same transaction managers.
 * See also {@link com.truward.tupl.support.AbstractTuplDaoSupport}.
 *
 * @author Alexander Shabanov
 */
public interface TuplTransactionManager {

  @Nonnull
  Database getDatabase();

  @Nonnull
  Transaction getTransaction();

  void commitTransaction(@Nonnull Transaction tx) throws IOException;

  void exitTransaction(@Nonnull Transaction tx) throws IOException;
}
