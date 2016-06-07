package com.truward.tupl.support.map.support;

import com.truward.tupl.support.AbstractTuplDatabaseSupport;
import com.truward.tupl.support.id.IdSupport;
import com.truward.tupl.support.load.ByteArrayResultMapper;
import com.truward.tupl.support.load.TuplLoadSupport;
import com.truward.tupl.support.map.PersistentMapDao;
import com.truward.tupl.support.transaction.TuplTransactionSupport;
import com.truward.tupl.support.update.TuplUpdateSupport;
import org.cojen.tupl.Database;
import org.cojen.tupl.Ordering;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Default implementation of {@link PersistentMapDao}.
 * @param <TValue> Value type
 */
public abstract class StandardPersistentMapDao<TValue>
    extends AbstractTuplDatabaseSupport
    implements PersistentMapDao<TValue>,
    IdSupport, TuplLoadSupport, TuplUpdateSupport, TuplTransactionSupport {

  private final String indexName;

  public StandardPersistentMapDao(@Nonnull Database db, @Nonnull String indexName) {
    super(db);
    this.indexName = indexName;
  }

  @Override
  public void put(@Nonnull String key, @Nonnull TValue value) {
    withTransaction(tx -> {
      storeObject(tx, indexName, key, toBytes(value));
      return null;
    });
  }

  @Override
  @Nullable
  public TValue get(@Nonnull String key, @Nonnull Supplier<TValue> defaultValueSupplier) {
    return withTransaction(tx -> loadObject(tx, indexName, key, this::toValue, defaultValueSupplier));
  }

  @Override
  public void delete(@Nonnull String key) {
    withTransaction(tx -> {
      deleteObject(tx, indexName, key);
      return null;
    });
  }

  @Nonnull
  @Override
  public List<Map.Entry<String, TValue>> getEntries(@Nullable String startKey, int limit, boolean ascending) {
    return withTransaction(tx -> loadInOrder(tx, indexName, ascending ? Ordering.ASCENDING : Ordering.DESCENDING,
        startKey,
        limit,
        (id, objectContents) -> new AbstractMap.SimpleImmutableEntry<>(id, toValue(id, objectContents))));
  }

  @Nonnull
  protected abstract byte[] toBytes(@Nonnull TValue value);

  @Nonnull
  protected abstract TValue toValue(@Nonnull String id, @Nonnull byte[] contents);
}
