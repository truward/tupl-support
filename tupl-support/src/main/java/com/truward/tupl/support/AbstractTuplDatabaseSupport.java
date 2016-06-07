package com.truward.tupl.support;

import org.cojen.tupl.Database;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Abstract base class for Tupl DAO implementations.
 *
 * @author Alexander Shabanov
 */
public abstract class AbstractTuplDatabaseSupport implements TuplDatabaseSupport {
  private final Database db;

  protected AbstractTuplDatabaseSupport(@Nonnull  Database db) {
    this.db = Objects.requireNonNull(db, "db");
  }

  @Nonnull
  @Override
  public final Database getDatabase() {
    return db;
  }
}
