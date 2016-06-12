package com.truward.tupl.protobuf;

import com.truward.tupl.support.map.PersistentMapDao;
import com.truward.tupl.support.transaction.TuplTransactionManager;
import com.truward.tupl.support.transaction.support.StandardTuplTransactionManager;
import com.truward.tupl.test.TuplTestUtil;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.io.IOException;

import static com.truward.tupl.protobuf.test.model.Bookstore.Author;
import static org.junit.Assert.assertEquals;

/**
 * @author Alexander Shabanov
 */
public final class ProtobufModelPersistentMapDaoTest {
  private PersistentMapDao<Author> authorMapDao;

  @Before
  public void init() throws IOException {
    final TuplTransactionManager txManager = new StandardTuplTransactionManager(TuplTestUtil.createTempDatabase());
    authorMapDao = new AuthorPersistentMapDao(txManager);
  }

  @Test
  public void shouldPersistAndRestoreAuthor() {
    // Given:
    final Author author = Author.newBuilder().setName("Author Name").setDescription("Description").build();
    final String authorKey = "1";

    // When:
    authorMapDao.put("1", author);

    // Then:
    assertEquals(author, authorMapDao.get(authorKey));
  }

  //
  // Private
  //

  private static final class AuthorPersistentMapDao extends ProtobufModelPersistentMapDao<Author> {

    AuthorPersistentMapDao(@Nonnull TuplTransactionManager txManager) {
      super(txManager, "Author");
    }

    @Nonnull
    @Override
    protected Author toValue(@Nonnull String id, @Nonnull byte[] contents) throws IOException {
      return Author.parseFrom(contents);
    }
  }
}
