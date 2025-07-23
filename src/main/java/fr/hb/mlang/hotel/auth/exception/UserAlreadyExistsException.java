package fr.hb.mlang.hotel.auth.exception;

public class UserAlreadyExistsException extends RuntimeException {

  public UserAlreadyExistsException() {
    super("User already exists");
  }
}
