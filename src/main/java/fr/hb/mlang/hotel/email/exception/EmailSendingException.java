package fr.hb.mlang.hotel.email.exception;

public class EmailSendingException extends RuntimeException {
  public EmailSendingException(String message, Throwable cause) {
    super(message, cause);
  }
}
