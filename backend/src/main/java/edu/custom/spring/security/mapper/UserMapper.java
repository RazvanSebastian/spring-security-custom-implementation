package edu.custom.spring.security.mapper;

import edu.custom.spring.security.model.entity.dto.security.UserClaimsDto;
import edu.custom.spring.security.model.entity.dto.security.UserDto;
import edu.custom.spring.security.model.entity.security.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    List<UserDto> mapTo(List<User> users);

    @Mapping(target = "givenName", expression = "java(user.getUserInfo().getGivenName())")
    UserDto mapTo(User user);

    @Mapping(target = "userInfo", expression = "java(user.getUserInfo())")
    @Mapping(source = "authorities", target = "grantedAuthorities")
    UserClaimsDto mapToUserClaims(User user);
}
