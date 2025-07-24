package fr.hb.mlang.hotel.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class RegisterRequestDTO {

  @Email
  @NotBlank
  private String email;

  @NotBlank
  @Size(min = 6, max = 64)
  private String password;
}