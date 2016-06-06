package com.truward.tupl.support.exception;

/**
 * Exception thrown when particular object can't be found.
 *
 * @author Alexander Shabanov
 */
public class ResultNotFoundException extends DaoException {
  public ResultNotFoundException() {
  }

  public ResultNotFoundException(String message) {
    super(message);
  }

  public ResultNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public ResultNotFoundException(Throwable cause) {
    super(cause);
  }
}
