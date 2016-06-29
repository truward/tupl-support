package com.truward.tupl.support.map;

import com.truward.tupl.support.id.Key;
import com.truward.tupl.support.transaction.support.StandardTuplTransactionManager;
import com.truward.tupl.test.TuplTestUtil;
import org.cojen.tupl.Database;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests for {@link PersistentMapDao}.
 *
 * @author Alexander Shabanov
 */
public final class PersistentMapDaoTest {

  private PersistentMapDao<String> mapDao;

  private static final List<Map.Entry<Key, String>> EMPTY_ENTRIES = Collections.<Map.Entry<Key, String>>emptyList();

  @Before
  public void init() throws IOException {
    final Database db = TuplTestUtil.createTempDatabase();
    mapDao = PersistentMapDao.newStringMap(new StandardTuplTransactionManager(db), "testMap");
  }

  @Test
  public void shouldAssociateAndGetBack() {
    // should get no entries
    assertNull(mapDao.get(Key.from("1")));

    // should get default entry
    final String defaultValue = "defaultValue";
    assertEquals(defaultValue, mapDao.get(Key.from("1"), () -> defaultValue));

    mapDao.put(Key.from("1"), "one");
    assertEquals("one", mapDao.get(Key.from("1")));

    // should get no entry after deleting it
    mapDao.delete(Key.from("1"));
    assertNull(mapDao.get(Key.from("1")));
  }

  @Test
  public void shouldEnumerateMap() {
    // empty map enumeration
    assertEquals(EMPTY_ENTRIES, mapDao.getEntries(null, 10));

    mapDao.put(Key.from("1"), "one");
    mapDao.put(Key.from("2"), "two");
    mapDao.put(Key.from("3"), "three");

    // ID-based pagination queries with zero limit
    assertEquals(EMPTY_ENTRIES, mapDao.getEntries(null, 0, true));
    assertEquals(EMPTY_ENTRIES, mapDao.getEntries(null, 0, false));

    // ascending ID-based pagination queries
    assertEquals(Arrays.asList(entry("1", "one"), entry("2", "two")),
        mapDao.getEntries(null, 2));
    assertEquals(Collections.singletonList(entry("3", "three")),
        mapDao.getEntries(Key.from("2"), 2));

    // descending ID-based pagination queries
    assertEquals(Arrays.asList(entry("3", "three"), entry("2", "two")),
        mapDao.getEntries(null, 2, false));
    assertEquals(Collections.singletonList(entry("1", "one")),
        mapDao.getEntries(Key.from("2"), 2, false));

    // Offset-based pagination queries with zero limit
    assertEquals(EMPTY_ENTRIES, mapDao.getEntries(0, 0, true));
    assertEquals(EMPTY_ENTRIES, mapDao.getEntries(0, 0, false));
    assertEquals(EMPTY_ENTRIES, mapDao.getEntries(1, 0, true));
    assertEquals(EMPTY_ENTRIES, mapDao.getEntries(1, 0, false));

    // ascending offset-based pagination queries
    assertEquals(Arrays.asList(entry("1", "one"), entry("2", "two")),
        mapDao.getEntries(0, 2));
    assertEquals(Collections.singletonList(entry("3", "three")),
        mapDao.getEntries(2, 2));

    // descending offset-based pagination queries
    assertEquals(Arrays.asList(entry("3", "three"), entry("2", "two")),
        mapDao.getEntries(0, 2, false));
    assertEquals(Collections.singletonList(entry("1", "one")),
        mapDao.getEntries(2, 2, false));

    // delete first entry and query
    mapDao.delete(Key.from("1"));
    assertEquals(Arrays.asList(entry("2", "two"), entry("3", "three")),
        mapDao.getEntries(null, 10));
  }

  //
  // Private
  //

  private static Map.Entry<Key, String> entry(String key, String value) {
    return new AbstractMap.SimpleImmutableEntry<>(Key.from(key), value);
  }
}
