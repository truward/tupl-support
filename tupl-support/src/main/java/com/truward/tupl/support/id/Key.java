package com.truward.tupl.support.id;

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
public final class Key implements Serializable {

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

  @Nonnull
  public static byte[] getKeyOrDefaultBytes(@Nullable Key key) {
    return key != null ? key.getBytes() : EMPTY_ID;
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

//  @Nonnull
//  public static Key from(@Nonnull byte[] keyArray, int pos, int size) {
//    if (size < 0) {
//      throw new IllegalArgumentException("size < 0");
//    }
//
//    if (size == 0) {
//      return EMPTY_KEY;
//    }
//
//    final byte[] key = new byte[size];
//    System.arraycopy(Objects.requireNonNull(keyArray, "keyArray"), pos, key, 0, size);
//    return inplace(key);
//  }

  @Nonnull
  public byte[] getBytes() {
    return key;
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(key);
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof Key &&
        (other == this || Arrays.equals(((Key) other).getBytes(), getBytes()));
  }

  @Override
  public String toString() {
    return DatatypeConverter.printHexBinary(key);
  }
}
