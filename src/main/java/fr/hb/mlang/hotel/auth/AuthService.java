package fr.hb.mlang.hotel.auth;

import fr.hb.mlang.hotel.auth.dto.AuthResponseDTO;
import fr.hb.mlang.hotel.auth.dto.RegisterRequestDTO;
import fr.hb.mlang.hotel.user.User;

public interface AuthService {

  AuthResponseDTO register(RegisterRequestDTO request);

  void validateUser(User user);

  void resetPassword(String email);

  void deleteAccount(User user);
}
