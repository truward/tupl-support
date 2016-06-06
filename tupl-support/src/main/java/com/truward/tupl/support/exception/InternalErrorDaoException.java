package com.truward.tupl.support.exception;

/**
 * Represents internal error in DAO.
 *
 * @author Alexander Shabanov
 */
public class InternalErrorDaoException extends DaoException {

  public InternalErrorDaoException() {
  }

  public InternalErrorDaoException(String message) {
    super(message);
  }

  public InternalErrorDaoException(String message, Throwable cause) {
    super(message, cause);
  }

  public InternalErrorDaoException(Throwable cause) {
    super(cause);
  }
}
