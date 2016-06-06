package com.truward.tupl.support.testModel;

/**
 * Book domain model.
 */
public final class Book extends AbstractBook<Author, Genre> {
  private static final long serialVersionUID = 1023L;

  public Book() {
  }

  public Book(BookUpdate bookUpdate) {
    setId(bookUpdate.getId());
    setTitle(bookUpdate.getTitle());
    setIsbn(bookUpdate.getIsbn());
    setPages(bookUpdate.getPages());
  }
}
