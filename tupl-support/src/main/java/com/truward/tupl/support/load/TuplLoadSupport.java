package com.truward.tupl.support.load;

import com.truward.tupl.support.exception.ResultNotFoundException;
import com.truward.tupl.support.id.IdOperations;
import com.truward.tupl.support.index.TuplIndexSupport;
import org.cojen.tupl.Cursor;
import org.cojen.tupl.Ordering;
import org.cojen.tupl.Transaction;
import org.cojen.tupl.View;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Support for load operations.
 *
 * @author Alexander Shabanov
 */
public interface TuplLoadSupport extends TuplIndexSupport, IdOperations {

  @Nullable
  default <T> T loadObject(@Nonnull Transaction tx,
                           @Nonnull String indexName,
                           @Nonnull String id,
                           @Nonnull ByteArrayResultMapper<T> mapper,
                           @Nonnull Supplier<T> defaultValueSupplier) {
    return withIndex(indexName, index -> {
      final byte[] result = index.load(tx, fromId(id));
      if (result == null) {
        return defaultValueSupplier.get();
      }

      return mapper.map(id, result);
    });
  }

  @Nonnull
  default <T> T loadObject(@Nonnull Transaction tx,
                           @Nonnull String indexName,
                           @Nonnull String id,
                           @Nonnull ByteArrayResultMapper<T> mapper) {
    return Objects.requireNonNull(loadObject(tx, indexName, id, mapper, () -> {
      throw new ResultNotFoundException("There is no object with id=" + id + " in index=" + indexName);
    }), () -> "Contract violation: null returned for id=" + id + ", index=" + indexName);
  }

  @Nonnull
  default <T> List<T> loadInOrder(@Nonnull Transaction tx,
                                  @Nonnull String indexName,
                                  @Nonnull Ordering ordering,
                                  @Nullable String startId,
                                  int limit,
                                  @Nonnull ByteArrayResultMapper<T> mapper) {
    return withIndex(indexName, index -> {
      final byte[] startKey = fromNullableId(startId);
      final View view;

      switch (ordering) {
        case ASCENDING:
          view = index.viewGt(startKey);
          break;
        case DESCENDING:
          if (startId != null) {
            view = index.viewLt(startKey);
          } else {
            // TODO: better way to get reverse view if no key has been set?
            view = index.viewGt(startKey).viewReverse();
          }

          break;
        default:
          throw new IllegalArgumentException("Invalid ordering=" + ordering);
      }

      final Cursor cursor = view.newCursor(tx);
      cursor.first();

      try {
        final List<T> result = new ArrayList<>(limit);
        for (int i = 0; i < limit; ++i) {
          final byte[] curKey = cursor.key();
          final byte[] curContents = cursor.value();
          if (curKey == null) {
            break;
          }

          result.add(mapper.map(toId(curKey), curContents));
          cursor.next();
        }
        return result;
      } finally {
        cursor.reset();
      }
    });
  }
}
