package com.truward.tupl.support.load;

import com.truward.tupl.support.id.Key;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * An interface, used by {@link TuplLoadSupport} for mapping returned byte array, associated with certain ID to
 * an object. Implementations of this interface perform the actual work of mapping each row to a result object.
 *
 * @author Alexander Shabanov
 */
public interface ByteArrayResultMapper<T> {

  @Nonnull
  T map(@Nonnull Key key, @Nonnull byte[] value) throws IOException;
}
