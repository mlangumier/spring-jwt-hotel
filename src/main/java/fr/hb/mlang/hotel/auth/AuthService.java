package fr.hb.mlang.hotel.auth;

import fr.hb.mlang.hotel.auth.dto.AuthResponseDTO;
import fr.hb.mlang.hotel.auth.dto.LoginRequestDTO;
import fr.hb.mlang.hotel.auth.dto.LoginResponseDTO;
import fr.hb.mlang.hotel.auth.dto.RegisterRequestDTO;
import fr.hb.mlang.hotel.auth.dto.TokenPairDTO;
import fr.hb.mlang.hotel.user.domain.User;

public interface AuthService {

  AuthResponseDTO register(RegisterRequestDTO request);

  AuthResponseDTO verifyAccount(String token);

  LoginResponseDTO login(LoginRequestDTO credentials);

  TokenPairDTO refreshToken(String refreshToken);

  void resetPassword(String email);

  void deleteAccount(User user);
}
