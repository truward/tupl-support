package com.truward.tupl.support.transaction.support;

import com.truward.tupl.support.transaction.TuplTransactionManager;
import org.cojen.tupl.Database;
import org.cojen.tupl.Transaction;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * An implementation of {@link TuplTransactionManager} that always creates new transaction.
 *
 * @author Alexander Shabanov
 */
public class AlwaysNewTuplTransactionManager extends TuplTransactionManagerBase implements TuplTransactionManager {

  public AlwaysNewTuplTransactionManager(@Nonnull Database db) {
    super(db);
  }

  @Nonnull
  @Override
  public Transaction getTransaction() {
    return db.newTransaction();
  }

  @Override
  public void commitTransaction(@Nonnull Transaction tx) throws IOException {
    tx.commit();
  }

  @Override
  public void exitTransaction(@Nonnull Transaction tx) throws IOException {
    tx.exit();
  }
}
