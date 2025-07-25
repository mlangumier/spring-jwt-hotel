package fr.hb.mlang.hotel.auth.dto;

public record TokenPairDTO(
    String accessToken,
    String refreshToken
) {

}
