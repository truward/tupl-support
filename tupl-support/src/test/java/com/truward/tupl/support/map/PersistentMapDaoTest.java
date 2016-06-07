package com.truward.tupl.support.map;

import com.truward.tupl.support.testUtil.TestDbUtil;
import org.cojen.tupl.Database;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Tests for {@link PersistentMapDao}.
 *
 * @author Alexander Shabanov
 */
public final class PersistentMapDaoTest {

  private PersistentMapDao<String> mapDao;

  @Before
  public void init() throws IOException {
    final Database db = TestDbUtil.createTempDb();
    mapDao = PersistentMapDao.newStringMap(db, "testMap");
  }

  @Test
  public void shouldAssociateAndGetBack() {
    // should get no entries
    assertNull(mapDao.get("1"));

    // should get default entry
    final String defaultValue = "defaultValue";
    assertEquals(defaultValue, mapDao.get("1", () -> defaultValue));

    mapDao.put("1", "one");
    assertEquals("one", mapDao.get("1"));

    // should get no entry after deleting it
    mapDao.delete("1");
    assertNull(mapDao.get("1"));
  }

  @Test
  public void shouldEnumerateMap() {
    // empty map enumeration
    List<Map.Entry<String, String>> entries = mapDao.getEntries(null, 10);
    assertTrue(entries.isEmpty());

    mapDao.put("1", "one");
    mapDao.put("2", "two");
    mapDao.put("3", "three");

    // ascending queries with zero limit
    entries = mapDao.getEntries(null, 0, true);
    assertTrue(entries.isEmpty());
    entries = mapDao.getEntries(null, 0, false);
    assertTrue(entries.isEmpty());

    // ascending queries
    entries = mapDao.getEntries(null, 2);
    assertEquals(Arrays.asList(entry("1", "one"), entry("2", "two")), entries);
    entries = mapDao.getEntries("2", 2);
    assertEquals(Collections.singletonList(entry("3", "three")), entries);

    // descending queries
    entries = mapDao.getEntries(null, 2, false);
    assertEquals(Arrays.asList(entry("3", "three"), entry("2", "two")), entries);
    entries = mapDao.getEntries("2", 2, false);
    assertEquals(Collections.singletonList(entry("1", "one")), entries);

    // delete first entry and query
    mapDao.delete("1");
    entries = mapDao.getEntries(null, 10);
    assertEquals(Arrays.asList(entry("2", "two"), entry("3", "three")), entries);
  }

  //
  // Private
  //

  private static Map.Entry<String, String> entry(String key, String value) {
    return new AbstractMap.SimpleImmutableEntry<>(key, value);
  }
}
