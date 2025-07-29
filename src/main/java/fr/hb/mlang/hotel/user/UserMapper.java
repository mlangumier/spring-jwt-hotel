package fr.hb.mlang.hotel.user;

import fr.hb.mlang.hotel.user.domain.User;
import fr.hb.mlang.hotel.user.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

  UserDTO fromUser(User user);

  User toUser(UserDTO userDTO);
}
