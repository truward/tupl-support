package com.truward.tupl.support.transaction.support;

import com.truward.tupl.support.transaction.TuplTransactionManager;
import org.cojen.tupl.Database;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Abstract base class for {@link TuplTransactionManager} implementations.
 *
 * @author Alexander Shabanov
 */
public abstract class TuplTransactionManagerBase implements TuplTransactionManager {
  protected final Database db;

  public TuplTransactionManagerBase(@Nonnull Database db) {
    this.db = Objects.requireNonNull(db, "db");
  }

  @Nonnull
  @Override
  public final Database getDatabase() {
    return db;
  }
}
