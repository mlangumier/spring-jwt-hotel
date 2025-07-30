package fr.hb.mlang.hotel.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordRequest(
    @NotBlank String email
) {

}
