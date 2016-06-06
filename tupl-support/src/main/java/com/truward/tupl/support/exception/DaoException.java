package com.truward.tupl.support.exception;

/**
 * Generic DAO exception.
 *
 * @author Alexander Shabanov
 */
public abstract class DaoException extends RuntimeException {

  public DaoException() {
  }

  public DaoException(String message) {
    super(message);
  }

  public DaoException(String message, Throwable cause) {
    super(message, cause);
  }

  public DaoException(Throwable cause) {
    super(cause);
  }
}
