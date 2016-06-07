package com.truward.tupl.support;

import com.truward.tupl.support.testDao.BookDao;
import com.truward.tupl.support.testDao.BookDaoImpl;
import com.truward.tupl.support.testModel.Author;
import com.truward.tupl.support.testModel.Book;
import com.truward.tupl.support.testModel.BookUpdate;
import com.truward.tupl.support.testModel.Genre;
import com.truward.tupl.support.testUtil.TestDbUtil;
import org.cojen.tupl.Database;
import org.cojen.tupl.DatabaseConfig;
import org.cojen.tupl.DurabilityMode;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Alexander Shabanov
 */
public final class TuplSupportTest {

  private BookDao bookDao;

  @Before
  public void init() throws IOException {
    bookDao = new BookDaoImpl(TestDbUtil.createTempDb());
  }

  @Test
  public void shouldSaveAndGetAuthor() {
    final Author author = new Author();
    author.setName("Jack London");
    author.setDescription("Famous novellist of early 20th century");

    // insert
    final String id = bookDao.saveAuthor(author);
    assertNotNull(id);

    author.setId(id);

    // retrieve/match #1
    final Author retAuthor1 = bookDao.getAuthor(id);
    assertEquals(author, retAuthor1);

    // update
    author.setName("Stephen King");
    author.setDescription("Modern writer in fantasy and horror genres");

    final String actualId = bookDao.saveAuthor(author);
    assertEquals(id, actualId);

    // retrieve/match #2
    final Author retAuthor2 = bookDao.getAuthor(id);
    assertEquals(author, retAuthor2);
  }

  @Test
  public void shouldGetNoAuthors() {
    final List<Author> authorList = bookDao.getAuthors(null, 10);
    assertTrue(authorList.isEmpty());
  }

  @Test
  public void shouldGetAuthors() {
    final Author author1 = addAuthor("Author 1");
    final Author author2 = addAuthor("Author 2");
    final Author author3 = addAuthor("Author 3");
    final Author author4 = addAuthor("Author 4");
    final Author author5 = addAuthor("Author 5");
    final Author author6 = addAuthor("Author 6");
    final Author author7 = addAuthor("Author 7");
    final Author author8 = addAuthor("Author 8");

    final List<Author> authorList = bookDao.getAuthors(null, 10);
    assertEquals(8, authorList.size());
    assertEquals(new HashSet<>(Arrays.asList(author1, author2, author3, author4, author5, author6, author7, author8)),
        new HashSet<>(authorList));

    // get same list, but w/pagination
    final List<Author> authorList1 = new ArrayList<>();
    {
      authorList1.addAll(bookDao.getAuthors(null, 4));
      assertEquals(4, authorList1.size());
      authorList1.addAll(bookDao
          .getAuthors(authorList1.get(authorList1.size() - 1).getId(), 4));
      assertEquals(8, authorList1.size());
      assertTrue(bookDao
          .getAuthors(authorList1.get(authorList1.size() - 1).getId(), 4).isEmpty());
    }

    assertEquals(authorList, authorList1);
  }

  @Test
  public void shouldSaveAndGetGenre() {
    final Genre genre = new Genre();
    genre.setShortName("sci-fi");
    genre.setLongName("Science Fiction");
    genre.setDescription("Science Fiction Genre");

    // insert
    final String id = bookDao.saveGenre(genre);
    assertNotNull(id);

    genre.setId(id);

    // retrieve/match #1
    final Genre retGenre1 = bookDao.getGenre(id);
    assertEquals(genre, retGenre1);

    // update
    genre.setShortName("fantasy");
    genre.setLongName("Fantasy");
    genre.setDescription("Fantasy");

    final String actualId = bookDao.saveGenre(genre);
    assertEquals(id, actualId);

    // retrieve/match #2
    final Genre retGenre2 = bookDao.getGenre(id);
    assertEquals(genre, retGenre2);
  }

  @Test
  public void shouldSaveAndGetBook() {
    final Genre sciFi = addGenre("sci-fi");
    final Genre novel = addGenre("novel");
    final Genre fantasy = addGenre("fantasy");
    final Genre detective = addGenre("detective");

    final Author jackLondon = addAuthor("Jack London");
    final Author stephenKing = addAuthor("Stephen King");

    final BookUpdate bookUpdate = new BookUpdate();
    bookUpdate.setTitle("The Sea Wolf");
    bookUpdate.setIsbn("111-2222-33333");
    bookUpdate.setPages(395);
    bookUpdate.setAuthors(Collections.singletonList(jackLondon.getId()));
    bookUpdate.setGenres(Collections.singletonList(novel.getId()));

    final String id = bookDao.saveBook(bookUpdate);

    final Book book = new Book(bookUpdate);
    book.setId(id);
    book.setAuthors(Collections.singletonList(jackLondon));
    book.setGenres(Collections.singletonList(novel));

    assertEquals(book, bookDao.getBook(id));

    bookUpdate.setId(id);
    bookUpdate.setTitle("Wolves of Calla");
    bookUpdate.setIsbn("777-8888-99999");
    bookUpdate.setPages(512);
    bookUpdate.setAuthors(Collections.singletonList(stephenKing.getId()));
    bookUpdate.setGenres(Arrays.asList(sciFi.getId(), fantasy.getId(), detective.getId()));

    final String actualId = bookDao.saveBook(bookUpdate);
    assertEquals(id, actualId);

    final Book book2 = new Book(bookUpdate);
    book2.setAuthors(Collections.singletonList(stephenKing));
    book2.setGenres(Arrays.asList(sciFi, fantasy, detective));

    assertEquals(book2, bookDao.getBook(id));
  }

  //
  // Private
  //

  private Genre addGenre(String genreName) {
    final Genre genre = new Genre();
    genre.setShortName(genreName);
    genre.setLongName(genreName + " Genre");
    genre.setDescription(genreName + " Description");

    final String id = bookDao.saveGenre(genre);
    genre.setId(id);
    return genre;
  }

  private Author addAuthor(String name) {
    final Author author = new Author();
    author.setName(name);
    author.setDescription(name + " Description");

    final String id = bookDao.saveAuthor(author);
    author.setId(id);
    return author;
  }
}
