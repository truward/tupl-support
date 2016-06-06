package com.truward.tupl.support.testDao;

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
  Author getAuthor(@Nonnull String id);

  @Nonnull
  String saveAuthor(@Nonnull Author author);

  @Nonnull
  List<Author> getAuthors(@Nullable String startId, int limit);

  @Nonnull
  Genre getGenre(@Nonnull String id);

  @Nonnull
  String saveGenre(@Nonnull Genre genre);

  @Nonnull
  Book getBook(@Nonnull String id);

  @Nonnull
  String saveBook(@Nonnull BookUpdate bookUpdate);
}
