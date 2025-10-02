package fr.hb.mlang.hotel.user.dto;

import fr.hb.mlang.hotel.user.domain.Role;

public record UserDTO(
    String id,
    String email,
    Role role,
    boolean valid
) {
}
