package com.truward.tupl.test;

import org.cojen.tupl.Database;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

/**
 * Tests for {@link TuplTestUtil}.
 *
 * @author Alexander Shabanov
 */
public final class TuplTestUtilTest {

  @Test
  public void shouldCreateTempDatabase() throws IOException {
    final Database db = TuplTestUtil.createTempDatabase();
    assertNotNull("db is null", db);
  }
}
