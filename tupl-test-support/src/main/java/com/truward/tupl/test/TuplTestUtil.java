package com.truward.tupl.test;

import org.cojen.tupl.Database;
import org.cojen.tupl.DatabaseConfig;
import org.cojen.tupl.DurabilityMode;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;

/**
 * Test utilities for integration tests for Tupl persistence layer.
 *
 * @author Alexander Shabanov
 */
public final class TuplTestUtil {
  private TuplTestUtil() {}

  /**
   * Creates database in temp folder with weakest durability mode for faster I/O.
   * This function assumes eventual cleanup of the temp folder.
   *
   * @return Newly created database
   * @throws IOException
   */
  @Nonnull
  public static Database createTempDatabase() throws IOException {
    final File baseFile = File.createTempFile("Test-", "-tupl");
    baseFile.deleteOnExit();

    // open new database, using weakest durability mode
    return Database.open(new DatabaseConfig()
        .baseFile(baseFile)
        .maxCacheSize(100_000L)
        .durabilityMode(DurabilityMode.NO_REDO));
  }
}
