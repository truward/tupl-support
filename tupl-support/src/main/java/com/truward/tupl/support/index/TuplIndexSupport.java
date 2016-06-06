package com.truward.tupl.support.index;

import com.truward.tupl.support.TuplDatabaseSupport;
import com.truward.tupl.support.exception.InternalErrorDaoException;
import org.cojen.tupl.Index;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * Support mixin for Tupl operations.
 *
 * @author Alexander Shabanov
 */
public interface TuplIndexSupport extends TuplDatabaseSupport {

  default <T> T withIndex(@Nonnull String indexName,
                          @Nonnull TuplIndexOperationCallback<T> callback) {
    try (final Index index = getDatabase().openIndex(indexName)) {
      return callback.call(index);
    } catch (IOException e) {
      throw new InternalErrorDaoException(e);
    }
  }
}
