package fr.hb.mlang.hotel.auth;

import org.springframework.security.core.userdetails.User;

public interface AuthBusiness {

  User register(User user);

  void validateUser(User user);

  void resetPassword(String email);

  void deleteAccount(User user);
}
