package com.truward.tupl.support.id;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Tests for {@link Key} class.
 *
 * @author Alexander Shabanov
 */
public final class KeyTest {

  @Test
  public void shouldCreateEmptyKey() {
    final byte[] arr = Key.getKeyOrDefaultBytes(null);
    assertArrayEquals(new byte[0], arr);
  }

  @Test
  public void shouldCreateSameKeys() {
    final Key k1 = Key.from("k1");
    final Key k2 = Key.from("k" + 1);

    assertEquals(k1, k2);
    assertEquals(0, k1.compareTo(k2));
    assertEquals(k1, Key.inplace(Arrays.copyOf(k1.getBytesNoCopy(), k1.getByteSize())));
  }

  @Test
  public void shouldCreateDifferentKeys() {
    final Key k1 = Key.from("k1");
    final Key k2 = Key.from("k2");

    assertNotSame(k1, k2);
    assertTrue(k1.compareTo(k2) < 0);
    assertTrue(k2.compareTo(k1) > 0);
  }

  @Test
  public void shouldStringifyKey() {
    final String k1Str = Key.from("k1").toString();
    final String k2Str = Key.EMPTY_KEY.toString();

    assertNotNull(k1Str);
    assertNotNull(k2Str);
  }

  @Test
  public void shouldConvertToAndFromBase64() {
    final Key key = Key.random();
    final String base64 = key.toBase64();

    assertNotNull(base64);
    assertEquals(key, Key.fromBase64(base64));
  }
}
