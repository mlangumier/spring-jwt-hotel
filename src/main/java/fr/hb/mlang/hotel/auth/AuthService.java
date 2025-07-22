package fr.hb.mlang.hotel.auth;


import fr.hb.mlang.hotel.user.User;

public interface AuthService {

  User register(User user);

  void validateUser(User user);

  void resetPassword(String email);

  void deleteAccount(User user);
}
