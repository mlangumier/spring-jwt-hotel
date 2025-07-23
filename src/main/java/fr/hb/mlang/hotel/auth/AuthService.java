package fr.hb.mlang.hotel.auth;

import fr.hb.mlang.hotel.auth.dto.AuthResponseDTO;
import fr.hb.mlang.hotel.auth.dto.RegisterRequestDTO;
import fr.hb.mlang.hotel.user.domain.User;

public interface AuthService {

  AuthResponseDTO register(RegisterRequestDTO request);

  AuthResponseDTO verifyAccount(String token);

  void resetPassword(String email);

  void deleteAccount(User user);
}
