package com.truward.tupl.support;

import org.cojen.tupl.Database;

import javax.annotation.Nonnull;

/**
 * An abstraction over entity that holds a reference to the Tupl database.
 *
 * @author Alexander Shabanov
 */
public interface TuplDatabaseProvider {

  @Nonnull
  Database getDatabase();
}
