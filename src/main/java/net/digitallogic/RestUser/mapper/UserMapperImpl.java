package net.digitallogic.RestUser.mapper;

import net.digitallogic.RestUser.persistence.model.UserEntity;
import net.digitallogic.RestUser.web.controller.request.CreateUserRequest;
import net.digitallogic.RestUser.web.controller.response.UserResponse;
import net.digitallogic.RestUser.web.dto.AuthorityDto;
import net.digitallogic.RestUser.web.dto.RoleDto;
import net.digitallogic.RestUser.web.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.Persistence;
import javax.persistence.PersistenceUtil;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class UserMapperImpl implements UserMapper {

    private final RoleMapper roleMapper;

    @Autowired
    public UserMapperImpl(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    @Override
    public UserDto toDto(CreateUserRequest user) {
        return UserDto.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .password(user.getPassword())
                .build();
    }

    @Override
    public UserDto toDto(UserEntity user) {
        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .accountExpired(!user.isAccountNonExpired())
                .accountLocked(!user.isAccountNonLocked())
                .credentialsExpired(!user.isCredentialsNonExpired())
                .accountEnabled(user.isEnabled())
                .build();

        PersistenceUtil pu = Persistence.getPersistenceUtil();

        if (pu.isLoaded(user.getRoles())) {
            userDto.setRoles(roleMapper.toDto(user.getRoles()));
        }
        return userDto;
    }

    @Override
    public UserResponse toUserResponse(UserDto user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .authorities(
                        user.getRoles().stream()
                            .map(RoleDto::getAuthorities)
                            .flatMap(List::stream)
                            .map(AuthorityDto::getAuthority)
                        .collect(Collectors.toSet())
                )
                .build();
    }

    @Override
    public UserEntity toEntity(UserDto user) {
        UserEntity userEntity = UserEntity.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();

        if (user.getId() != null)
            userEntity.setId(user.getId());

        return userEntity;
    }

    @Override
    public UserEntity updateEntity(UserEntity entity, UserDto updateInfo) {
        if (updateInfo.getEmail() != null)
            entity.setEmail(updateInfo.getEmail());
        if (updateInfo.getFirstName() != null)
            entity.setFirstName(updateInfo.getFirstName());
        if (updateInfo.getLastName() != null)
            entity.setLastName(updateInfo.getLastName());

        return entity;
    }

    @Override
    public List<UserDto> toDto(Iterable<UserEntity> users) {
        return StreamSupport.stream(users.spliterator(), false)
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserEntity> toEntity(Iterable<UserDto> users) {
        return StreamSupport.stream(users.spliterator(), false)
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponse> toUserResponse(Iterable<UserDto> users) {
        return StreamSupport.stream(users.spliterator(), false)
                .map(this::toUserResponse)
                .collect(Collectors.toList());
    }
}
