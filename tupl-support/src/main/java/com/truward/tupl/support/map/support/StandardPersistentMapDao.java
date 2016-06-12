package com.truward.tupl.support.map.support;

import com.truward.tupl.support.AbstractTuplDaoSupport;
import com.truward.tupl.support.id.IdSupport;
import com.truward.tupl.support.load.TuplLoadSupport;
import com.truward.tupl.support.map.PersistentMapDao;
import com.truward.tupl.support.transaction.TuplTransactionManager;
import com.truward.tupl.support.update.TuplUpdateSupport;
import org.cojen.tupl.Ordering;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Default implementation of {@link PersistentMapDao}.
 * @param <TValue> Value type
 */
public abstract class StandardPersistentMapDao<TValue> extends AbstractTuplDaoSupport
    implements PersistentMapDao<TValue>, IdSupport, TuplLoadSupport, TuplUpdateSupport {

  private final String indexName;

  public StandardPersistentMapDao(@Nonnull TuplTransactionManager txManager, @Nonnull String indexName) {
    super(txManager);
    this.indexName = indexName;
  }

  @Override
  public void put(@Nonnull String key, @Nonnull TValue value) {
    storeObject(indexName, key, toBytes(value));
  }

  @Override
  @Nullable
  public TValue get(@Nonnull String key, @Nonnull Supplier<TValue> defaultValueSupplier) {
    return loadObject(indexName, key, this::toValue, defaultValueSupplier);
  }

  @Override
  public void delete(@Nonnull String key) {
    deleteObject(indexName, key);
  }

  @Nonnull
  @Override
  public List<Map.Entry<String, TValue>> getEntries(@Nullable String startKey, int offset, int limit, boolean ascending) {
    return loadInOrder(indexName, ascending ? Ordering.ASCENDING : Ordering.DESCENDING,
        startKey,
        offset,
        limit,
        (id, objectContents) -> new AbstractMap.SimpleImmutableEntry<>(id, toValue(id, objectContents)));
  }

  @Nonnull
  protected abstract byte[] toBytes(@Nonnull TValue value);

  @Nonnull
  protected abstract TValue toValue(@Nonnull String id, @Nonnull byte[] contents) throws IOException;
}
