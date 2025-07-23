package fr.hb.mlang.hotel.auth.exception;

public class VerifyTokenException extends RuntimeException {

  public VerifyTokenException(String message) {
    super(message);
  }
}
