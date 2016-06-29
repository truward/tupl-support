package com.truward.tupl.support.testDao;

import com.truward.tupl.support.AbstractTuplDaoSupport;
import com.truward.tupl.support.id.Key;
import com.truward.tupl.support.load.TuplLoadSupport;
import com.truward.tupl.support.testModel.*;
import com.truward.tupl.support.transaction.TuplTransactionManager;
import com.truward.tupl.support.update.TuplUpdateSupport;
import org.cojen.tupl.Ordering;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Default implementation of {@link BookDao}.
 *
 * @author Alexander Shabanov
 */
public class BookDaoImpl extends AbstractTuplDaoSupport implements BookDao, TuplUpdateSupport, TuplLoadSupport {

  private static final String AUTHOR_INDEX = "Author";
  private static final String GENRE_INDEX = "Genre";
  private static final String BOOK_INDEX = "Book";

  public BookDaoImpl(@Nonnull TuplTransactionManager txManager) {
    super(txManager);
  }

  @Nonnull
  @Override
  public Author getAuthor(@Nonnull Key id) {
    return loadObject(AUTHOR_INDEX, id, new TestModelBase.ResultMapper<>(Author.class));
  }

  @Nonnull
  @Override
  public Key saveAuthor(@Nonnull Author author) {
    return updateObject(AUTHOR_INDEX, author.getId(), author.toBytes());
  }

  @Nonnull
  @Override
  public List<Author> getAuthors(@Nullable Key startId, int limit) {
    return loadInOrder(AUTHOR_INDEX, Ordering.ASCENDING, startId, 0, limit, new TestModelBase.ResultMapper<>(Author.class));
  }

  @Nonnull
  @Override
  public Genre getGenre(@Nonnull Key id) {
    return loadObject(GENRE_INDEX, id, new TestModelBase.ResultMapper<>(Genre.class));
  }

  @Nonnull
  @Override
  public Key saveGenre(@Nonnull Genre genre) {
    return updateObject(GENRE_INDEX, genre.getId(), genre.toBytes());
  }

  @Nonnull
  @Override
  public Book getBook(@Nonnull Key id) {
    return loadObject(BOOK_INDEX, id, (bookId, result) -> {
      final BookUpdate bookUpdate = new TestModelBase.ResultMapper<>(BookUpdate.class).map(bookId, result);
      final Book r = new Book();
      r.setId(id);
      r.setTitle(bookUpdate.getTitle());
      r.setIsbn(bookUpdate.getIsbn());
      r.setPages(bookUpdate.getPages());
      r.setAuthors(bookUpdate.getAuthors()
          .stream()
          .map(this::getAuthor)
          .collect(Collectors.toList()));
      r.setGenres(bookUpdate.getGenres()
          .stream()
          .map(this::getGenre)
          .collect(Collectors.toList()));
      return r;
    });
  }

  @Nonnull
  @Override
  public Key saveBook(@Nonnull BookUpdate bookUpdate) {
    return updateObject(BOOK_INDEX, bookUpdate.getId(), bookUpdate.toBytes());
  }
}
