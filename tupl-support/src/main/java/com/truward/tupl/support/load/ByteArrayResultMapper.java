package com.truward.tupl.support.load;

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
  T map(@Nonnull String id, @Nonnull byte[] objectContents) throws IOException;
}
