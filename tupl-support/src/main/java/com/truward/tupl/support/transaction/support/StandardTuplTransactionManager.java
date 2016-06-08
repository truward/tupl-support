package com.truward.tupl.support.transaction.support;

import org.cojen.tupl.Database;
import org.cojen.tupl.Transaction;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * Standard transaction manager.
 *
 * @author Alexander Shabanov
 */
public class StandardTuplTransactionManager extends TuplTransactionManagerBase {

  private final ThreadLocal<TransactionHolder> threadLocalTxHolder = new ThreadLocal<TransactionHolder>() {
    @Override
    protected TransactionHolder initialValue() {
      return new TransactionHolder();
    }
  };

  public StandardTuplTransactionManager(@Nonnull Database db) {
    super(db);
  }

  @Nonnull
  @Override
  public Transaction getTransaction() {
    final TransactionHolder holder = threadLocalTxHolder.get();
    if (holder.tx != null) {
      assert holder.usages > 0;
      ++holder.usages;
    } else {
      assert holder.usages == 0;
      holder.tx = getDatabase().newTransaction();
      holder.usages = 1;
    }

    return holder.tx;
  }

  @Override
  public void commitTransaction(@Nonnull Transaction tx) throws IOException {
    final TransactionHolder holder = threadLocalTxHolder.get();
    holder.checkTransactionAvailability();

    if (holder.usages == 1) {
      holder.tx.commit(); // commit only if this is the last usage
    }
  }

  @Override
  public void exitTransaction(@Nonnull Transaction tx) throws IOException {
    final TransactionHolder holder = threadLocalTxHolder.get();
    holder.checkTransactionAvailability();

    --holder.usages;
    if (holder.usages == 0) {
      // this is the last transaction usage
      holder.tx.exit();
      holder.tx = null;
    }
  }

  //
  // Private
  //



  private static final class TransactionHolder {
    private Transaction tx;
    private int usages;

    void checkTransactionAvailability() {
      if (tx == null || usages <= 0) {
        throw new IllegalStateException("Invalid state for transaction holder"); // should not happen
      }
    }
  }
}
