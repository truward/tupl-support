package com.truward.tupl.support.map;

import com.truward.tupl.support.id.Key;
import com.truward.tupl.support.map.support.StandardPersistentMapDao;
import com.truward.tupl.support.transaction.TuplTransactionManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * An interface to simple DAO, that stores String-to-Value associations in Tupl, where value can be arbitrary type.
 * This DAO is useful for simple tables, that store indexed fields.
 *
 * @param <TValue> value type
 *
 * @author Alexander Shabanov
 */
public interface PersistentMapDao<TValue> {

  void put(@Nonnull Key key, @Nonnull TValue value);

  @Nullable
  TValue get(@Nonnull Key key, @Nonnull Supplier<TValue> defaultValueSupplier);

  @Nullable
  default TValue get(@Nonnull Key key) {
    return get(key, () -> null);
  }

  void delete(@Nonnull Key key);

  @Nonnull
  List<Map.Entry<Key, TValue>> getEntries(@Nullable Key startKey, int offset, int limit, boolean ascending);

  @Nonnull
  default List<Map.Entry<Key, TValue>> getEntries(@Nullable Key startKey, int limit, boolean ascending) {
    return getEntries(startKey, 0, limit, ascending);
  }

  @Nonnull
  default List<Map.Entry<Key, TValue>> getEntries(@Nullable Key startKey, int limit) {
    return getEntries(startKey, limit, true);
  }

  @Nonnull
  default List<Map.Entry<Key, TValue>> getEntries(int offset, int limit, boolean ascending) {
    return getEntries(null, offset, limit, ascending);
  }

  @Nonnull
  default List<Map.Entry<Key, TValue>> getEntries(int offset, int limit) {
    return getEntries(offset, limit, true);
  }

  /**
   * Creates an instance of persistent map dao, that stores Key-to-String associations.
   *
   * @param txManager Transaction manager
   * @param indexName Index name
   * @return Map instance
   */
  static PersistentMapDao<String> newStringMap(@Nonnull TuplTransactionManager txManager, @Nonnull String indexName) {
    return new StandardPersistentMapDao<String>(txManager, indexName) {
      @Nonnull
      @Override
      protected byte[] toBytes(@Nonnull String s) {
        return s.getBytes(StandardCharsets.UTF_8);
      }

      @Nonnull
      @Override
      protected String toValue(@Nonnull Key id, @Nonnull byte[] contents) {
        return new String(contents, StandardCharsets.UTF_8);
      }
    };
  }

  /**
   * Creates an instance of persistent map dao, that stores Key-to-byte[] associations.
   *
   * @param txManager Transaction manager
   * @param indexName Index name
   * @return Map instance
   */
  static PersistentMapDao<byte[]> newByteArrayMap(@Nonnull TuplTransactionManager txManager, @Nonnull String indexName) {
    return new StandardPersistentMapDao<byte[]>(txManager, indexName) {
      @Nonnull
      @Override
      protected byte[] toBytes(@Nonnull byte[] bytes) {
        return bytes;
      }

      @Nonnull
      @Override
      protected byte[] toValue(@Nonnull Key id, @Nonnull byte[] contents) throws IOException {
        return contents;
      }
    };
  }
}
