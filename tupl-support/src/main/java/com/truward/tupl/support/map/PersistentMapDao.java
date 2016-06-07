package com.truward.tupl.support.map;

import com.truward.tupl.support.map.support.StandardPersistentMapDao;
import org.cojen.tupl.Database;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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

  void put(@Nonnull String key, @Nonnull TValue value);

  @Nullable
  TValue get(@Nonnull String key, @Nonnull Supplier<TValue> defaultValueSupplier);

  @Nullable
  default TValue get(@Nonnull String key) {
    return get(key, () -> null);
  }

  void delete(@Nonnull String key);

  @Nonnull
  List<Map.Entry<String, TValue>> getEntries(@Nullable String startKey, int limit, boolean ascending);

  @Nonnull
  default List<Map.Entry<String, TValue>> getEntries(@Nullable String startKey, int limit) {
    return getEntries(startKey, limit, true);
  }

  /**
   * Creates an instance of persistent map dao, that stores String-to-String associations.
   *
   * @param db Database, where map is stored.
   * @param indexName Index name
   * @return Map instance
   */
  static PersistentMapDao<String> newStringMap(@Nonnull Database db, @Nonnull String indexName) {
    return new StandardPersistentMapDao<String>(db, indexName) {
      @Nonnull
      @Override
      protected byte[] toBytes(@Nonnull String s) {
        return s.getBytes(StandardCharsets.UTF_8);
      }

      @Nonnull
      @Override
      protected String toValue(@Nonnull String id, @Nonnull byte[] contents) {
        return new String(contents, StandardCharsets.UTF_8);
      }
    };
  }
}
