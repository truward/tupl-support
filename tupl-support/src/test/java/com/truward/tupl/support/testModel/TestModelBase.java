package com.truward.tupl.support.testModel;

import com.truward.tupl.support.load.ByteArrayResultMapper;

import javax.annotation.Nonnull;
import java.io.*;

/**
 * Base class for test domain model.
 * NOTE: instances of this class use Serializable infrastructure for converting to and from byte array which
 * is stored in underlying Tupl DB. This is usually a bad choice in real world programs as it makes extension
 * really hard.
 * In real world app you might want to use extensible binary encoding formats, such as Google's protocol buffers, or
 * Thrift.
 *
 * @author Alexander Shabanov
 */
public interface TestModelBase extends Serializable {

  void setId(String id);

  final class ResultMapper<T extends TestModelBase> implements ByteArrayResultMapper<T> {
    final Class<T> clazz;

    public ResultMapper(@Nonnull Class<T> clazz) {
      this.clazz = clazz;
    }

    @Nonnull
    @Override
    public T map(@Nonnull String id, @Nonnull byte[] objectContents) throws IOException {
      try (final ByteArrayInputStream is = new ByteArrayInputStream(objectContents)) {
        try (final ObjectInputStream ois = new ObjectInputStream(is)) {
          final T result = clazz.cast(ois.readObject());
          result.setId(id);
          return result;
        } catch (ClassNotFoundException e) {
          throw new IOException("Unable to deserialize an instance of " + clazz, e);
        }
      }
    }
  }

  default byte[] toBytes() {
    try (final ByteArrayOutputStream os = new ByteArrayOutputStream()) {
      try (final ObjectOutputStream oos = new ObjectOutputStream(os)) {
        oos.writeObject(this);
      }
      return os.toByteArray();
    } catch (IOException e) {
      throw new IllegalStateException(e); // should not happen
    }
  }
}
