package com.truward.tupl.support.exception;

/**
 * An error, thrown when given key has not been found.
 *
 * @author Alexander Shabanov
 */
public class KeyNotFoundException extends DaoException {

  public KeyNotFoundException() {
  }

  public KeyNotFoundException(String message) {
    super(message);
  }

  public KeyNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public KeyNotFoundException(Throwable cause) {
    super(cause);
  }
}
