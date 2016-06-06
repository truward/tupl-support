package com.truward.tupl.support.index;

import org.cojen.tupl.Index;

import java.io.IOException;

/**
 * Encapsulates callback, that abstracts out operations performed on Tupl index.
 *
 * @param <T> Result, that should be returned as a result of index operation
 *
 * @author Alexander Shabanov
 */
public interface TuplIndexOperationCallback<T> {
  T call(Index index) throws IOException;
}
