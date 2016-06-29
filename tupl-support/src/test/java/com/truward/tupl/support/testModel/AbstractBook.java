package com.truward.tupl.support.testModel;

import com.truward.tupl.support.id.Key;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * @author Alexander Shabanov
 */
public abstract class AbstractBook<TAuthor, TGenre> implements TestModelBase {

  private Key id;
  private String title = "";
  private String isbn = "";
  private int pages = 0;
  private List<TAuthor> authors = Collections.emptyList();
  private List<TGenre> genres = Collections.emptyList();

  @Nullable
  public Key getId() {
    return id;
  }

  @Override
  public void setId(@Nullable Key id) {
    this.id = id;
  }

  @Nonnull
  public String getTitle() {
    return title;
  }

  public void setTitle(@Nonnull String title) {
    this.title = title;
  }

  @Nonnull
  public String getIsbn() {
    return isbn;
  }

  public void setIsbn(@Nonnull String isbn) {
    this.isbn = isbn;
  }

  public int getPages() {
    return pages;
  }

  public void setPages(int pages) {
    this.pages = pages;
  }

  @Nonnull
  public List<TAuthor> getAuthors() {
    return authors;
  }

  public void setAuthors(@Nonnull List<TAuthor> authors) {
    this.authors = authors;
  }

  @Nonnull
  public List<TGenre> getGenres() {
    return genres;
  }

  public void setGenres(@Nonnull List<TGenre> genres) {
    this.genres = genres;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || (!(o instanceof AbstractBook))) return false;

    AbstractBook book = (AbstractBook) o;

    return pages == book.pages && (id != null ? id.equals(book.id) : book.id == null &&
        (title != null ? title.equals(book.title) : book.title == null &&
            (isbn != null ? isbn.equals(book.isbn) : book.isbn == null &&
                (authors != null ? authors.equals(book.authors) : book.authors == null &&
                    (genres != null ? genres.equals(book.genres) : book.genres == null)))));

  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (title != null ? title.hashCode() : 0);
    result = 31 * result + (isbn != null ? isbn.hashCode() : 0);
    result = 31 * result + pages;
    result = 31 * result + (authors != null ? authors.hashCode() : 0);
    result = 31 * result + (genres != null ? genres.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Book{" +
        "id='" + getId() + '\'' +
        ", title='" + getTitle() + '\'' +
        ", isbn='" + getIsbn() + '\'' +
        ", pages=" + getPages() +
        ", authors=" + getAuthors() +
        ", genres=" + getGenres() +
        '}';
  }
}
