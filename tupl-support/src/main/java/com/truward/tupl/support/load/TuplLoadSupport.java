package com.truward.tupl.support.load;

import com.truward.tupl.support.exception.ResultNotFoundException;
import com.truward.tupl.support.id.Key;
import com.truward.tupl.support.index.TuplIndexSupport;
import com.truward.tupl.support.transaction.TuplTransactionSupport;
import org.cojen.tupl.Cursor;
import org.cojen.tupl.Ordering;
import org.cojen.tupl.View;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Support for load operations.
 *
 * @author Alexander Shabanov
 */
public interface TuplLoadSupport extends TuplTransactionSupport, TuplIndexSupport {

  @Nullable
  default <T> T loadObject(@Nonnull String indexName,
                           @Nonnull Key id,
                           @Nonnull ByteArrayResultMapper<T> mapper,
                           @Nonnull Supplier<T> defaultValueSupplier) {
    return withTransaction(tx -> withIndex(indexName, index -> {
      final byte[] result = index.load(tx, id.getBytesNoCopy());
      if (result == null) {
        return defaultValueSupplier.get();
      }

      return mapper.map(id, result);
    }));
  }

  @Nonnull
  default <T> T loadObject(@Nonnull String indexName,
                           @Nonnull Key id,
                           @Nonnull ByteArrayResultMapper<T> mapper) {
    return Objects.requireNonNull(loadObject(indexName, id, mapper, () -> {
      throw new ResultNotFoundException("There is no object with id=" + id + " in index=" + indexName);
    }), () -> "Contract violation: null returned for id=" + id + ", index=" + indexName);
  }

  @Nonnull
  default <T> List<T> loadInOrder(@Nonnull String indexName,
                                  @Nonnull Ordering ordering,
                                  @Nullable Key startId,
                                  int offset,
                                  int limit,
                                  @Nonnull ByteArrayResultMapper<T> mapper) {
    if (offset < 0) {
      throw new IllegalArgumentException("offset < 0");
    }

    if (limit < 0) {
      throw new IllegalArgumentException("limit < 0");
    }

    if (limit == 0) {
      return Collections.emptyList();
    }

    return withTransaction(tx -> withIndex(indexName, index -> {
      final byte[] startKey = Key.getKeyOrDefaultBytes(startId);
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
      if (offset != 0) {
        cursor.skip(offset);
      }

      try {
        final List<T> result = new ArrayList<>();
        for (int i = 0; i < limit; ++i) {
          final byte[] curKey = cursor.key();
          final byte[] curValue = cursor.value();
          if (curKey == null) {
            break;
          }

          result.add(mapper.map(Key.inplace(curKey), curValue));
          cursor.next();
        }
        return result;
      } finally {
        cursor.reset();
      }
    }));
  }
}
