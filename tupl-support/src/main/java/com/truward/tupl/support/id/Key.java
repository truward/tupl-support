package com.truward.tupl.support.id;

import org.cojen.tupl.io.Utils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.bind.DatatypeConverter;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Tupl key wrapper around byte array.
 *
 * @author Alexander Shabanov
 */
public final class Key implements Serializable, Comparable<Key> {

  private static final long serialVersionUID = 25431698756923921L;

  public static final int DEFAULT_ID_LENGTH = 12;
  public static final byte[] EMPTY_ID = new byte[0];
  public static final Key EMPTY_KEY = new Key(EMPTY_ID);

  private final byte[] key;

  private Key(byte[] key) {
    this.key = Objects.requireNonNull(key, "Key array is null");
  }

  @Nonnull
  public static Key inplace(byte[] arr) {
    return new Key(arr);
  }

  /**
   * Returns internal byte array that constitutes the given key or empty byte array if key is null
   *
   * @param key Key
   * @return Key's byte array or empty byte array if given key is null
   */
  @Nonnull
  public static byte[] getKeyOrDefaultBytes(@Nullable Key key) {
    return key != null ? key.getBytesNoCopy() : EMPTY_ID;
  }

  @Nonnull
  public static Key random(@Nonnull Random random, int length) {
    if (length < 0) {
      throw new IllegalArgumentException("length < 0");
    }

    if (length == 0) {
      return EMPTY_KEY;
    }

    final byte[] bytes = new byte[length];
    random.nextBytes(bytes);
    return inplace(bytes);
  }

  @Nonnull
  public static Key random() {
    return random(ThreadLocalRandom.current(), DEFAULT_ID_LENGTH);
  }

  @Nonnull
  public static Key from(@Nonnull String value) {
    return new Key(value.getBytes(StandardCharsets.UTF_8));
  }

  /**
   * Returns associated internal byte array that constitutes this key without making extra copy for safety.
   * <p>
   * WARNING: caller should not modify contents of the returned byte array.
   * </p>
   *
   * @return Byte array, that constitutes this key, can be empty
   */
  @Nonnull
  public byte[] getBytesNoCopy() {
    return key;
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(key);
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof Key &&
        (other == this || Arrays.equals(((Key) other).getBytesNoCopy(), getBytesNoCopy()));
  }

  @Override
  public String toString() {
    return DatatypeConverter.printHexBinary(key);
  }

  /**
   * Compares this key to the other using Tupl helper method for comparing unsigned arrays.
   *
   * {@inheritDoc}
   */
  @Override
  public int compareTo(@Nonnull Key other) {
    final byte[] otherKeyBytes = Objects.requireNonNull(other, "otherKey").getBytesNoCopy();
    return Utils.compareUnsigned(getBytesNoCopy(), otherKeyBytes);
  }
}
