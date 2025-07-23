package fr.hb.mlang.hotel.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(
    @Email @NotBlank String email,
    @NotBlank @Size(min = 4, max = 64) String password
) { }