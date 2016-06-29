package com.truward.tupl.support.testDao;

import com.truward.tupl.support.id.Key;
import com.truward.tupl.support.testModel.Author;
import com.truward.tupl.support.testModel.Book;
import com.truward.tupl.support.testModel.BookUpdate;
import com.truward.tupl.support.testModel.Genre;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Book DAO contract.
 */
public interface BookDao {

  @Nonnull
  Author getAuthor(@Nonnull Key id);

  @Nonnull
  Key saveAuthor(@Nonnull Author author);

  @Nonnull
  List<Author> getAuthors(@Nullable Key startId, int limit);

  @Nonnull
  Genre getGenre(@Nonnull Key id);

  @Nonnull
  Key saveGenre(@Nonnull Genre genre);

  @Nonnull
  Book getBook(@Nonnull Key id);

  @Nonnull
  Key saveBook(@Nonnull BookUpdate bookUpdate);
}
