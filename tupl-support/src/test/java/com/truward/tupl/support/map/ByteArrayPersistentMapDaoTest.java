package com.truward.tupl.support.map;

import com.truward.tupl.support.id.Key;
import com.truward.tupl.support.transaction.support.StandardTuplTransactionManager;
import com.truward.tupl.test.TuplTestUtil;
import org.cojen.tupl.Database;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author Alexander Shabanov
 */
public final class ByteArrayPersistentMapDaoTest {
  private PersistentMapDao<byte[]> mapDao;

  private static final List<Map.Entry<Key, byte[]>> EMPTY_ENTRIES = Collections.<Map.Entry<Key, byte[]>>emptyList();

  @Before
  public void init() throws IOException {
    final Database db = TuplTestUtil.createTempDatabase();
    mapDao = PersistentMapDao.newByteArrayMap(new StandardTuplTransactionManager(db), "testByteArrayMap");
  }

  @Test
  public void shouldCrudValuesInMap() {
    assertEquals(EMPTY_ENTRIES, mapDao.getEntries(0, 10));

    final Key key = Key.inplace(new byte[] { 1, 2, 3 });
    final byte[] value = new byte[] { 4, 5, 6, 7, 8 };
    mapDao.put(key, value);

    assertArrayEquals(value, mapDao.get(key));
    final byte[] anotherValue = new byte[] { 0 };
    mapDao.put(key, anotherValue);
    assertArrayEquals(anotherValue, mapDao.get(key));

    final List<Map.Entry<Key, byte[]>> entries = mapDao.getEntries(0, 10);
    assertEquals(1, entries.size());
    assertArrayEquals(anotherValue, entries.get(0).getValue());

    mapDao.delete(key);

    assertNull(mapDao.get(key));
  }
}
