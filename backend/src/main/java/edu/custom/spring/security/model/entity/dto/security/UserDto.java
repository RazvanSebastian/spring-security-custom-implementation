package edu.custom.spring.security.model.entity.dto.security;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDto {

    private Long id;
    private String username;
    private String givenName;

}
