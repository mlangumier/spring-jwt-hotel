package fr.hb.mlang.hotel.auth.dto;

import fr.hb.mlang.hotel.user.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
  private String accessToken;
  private UserDTO user;
}
