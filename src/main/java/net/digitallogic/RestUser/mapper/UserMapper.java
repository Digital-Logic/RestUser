package net.digitallogic.RestUser.mapper;

import net.digitallogic.RestUser.persistence.model.UserEntity;
import net.digitallogic.RestUser.web.controller.request.CreateUserRequest;
import net.digitallogic.RestUser.web.controller.response.UserResponse;
import net.digitallogic.RestUser.web.dto.UserDto;

import java.util.List;

public interface UserMapper {
    UserDto toDto(CreateUserRequest user);
    UserDto toDto(UserEntity user);
    UserEntity toEntity(UserDto user);
    UserEntity updateEntity(UserEntity entity, UserDto updateInfo);
    UserResponse toUserResponse(UserDto user);
    List<UserDto> toDto(Iterable<UserEntity> users);
    List<UserEntity> toEntity(Iterable<UserDto> users);
    List<UserResponse> toUserResponse(Iterable<UserDto> users);
}
