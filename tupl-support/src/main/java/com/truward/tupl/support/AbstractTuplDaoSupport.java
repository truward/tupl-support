package com.truward.tupl.support;

import com.truward.tupl.support.transaction.TuplTransactionManager;
import com.truward.tupl.support.transaction.TuplTransactionSupport;
import org.cojen.tupl.Database;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Abstract base class for Tupl DAO implementations.
 *
 * @author Alexander Shabanov
 */
public abstract class AbstractTuplDaoSupport implements TuplTransactionSupport {
  private final TuplTransactionManager txManager;

  protected AbstractTuplDaoSupport(@Nonnull TuplTransactionManager txManager) {
    this.txManager = Objects.requireNonNull(txManager, "txManager");
  }

  @Nonnull
  @Override
  public final Database getDatabase() {
    return txManager.getDatabase();
  }

  @Nonnull
  @Override
  public final TuplTransactionManager getTransactionManager() {
    return txManager;
  }
}
