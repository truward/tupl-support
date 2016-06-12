package com.truward.tupl.protobuf;

import com.google.protobuf.Message;
import com.truward.tupl.support.map.support.StandardPersistentMapDao;
import com.truward.tupl.support.transaction.TuplTransactionManager;

import javax.annotation.Nonnull;

/**
 * Base class for Tupl DAO, working with protobuf objects.
 *
 * @author Alexander Shabanov
 */
public abstract class ProtobufModelPersistentMapDao<TValue extends Message> extends StandardPersistentMapDao<TValue> {

  protected ProtobufModelPersistentMapDao(@Nonnull TuplTransactionManager txManager, @Nonnull String indexName) {
    super(txManager, indexName);
  }

  @Nonnull
  @Override
  protected final byte[] toBytes(@Nonnull TValue value) {
    return value.toByteArray();
  }
}
