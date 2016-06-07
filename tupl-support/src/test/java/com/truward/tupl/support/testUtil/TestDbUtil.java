package com.truward.tupl.support.testUtil;

import org.cojen.tupl.Database;
import org.cojen.tupl.DatabaseConfig;
import org.cojen.tupl.DurabilityMode;

import java.io.File;
import java.io.IOException;

/**
 * Helper class for creating temporary databases.
 */
public final class TestDbUtil {
  private TestDbUtil() {} // hidden

  public static Database createTempDb() throws IOException {
    final File tmpFile = File.createTempFile("Test-", "-db");

    // open new database, using weakest durability mode
    return Database.open(new DatabaseConfig()
        .baseFilePath(tmpFile.getPath())
        .maxCacheSize(100_000L)
        .durabilityMode(DurabilityMode.NO_REDO));
  }
}
