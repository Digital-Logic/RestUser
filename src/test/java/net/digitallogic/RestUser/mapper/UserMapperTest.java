package net.digitallogic.RestUser.mapper;

import net.digitallogic.RestUser.fixtures.RoleFixtures;
import net.digitallogic.RestUser.fixtures.UserFixtures;
import net.digitallogic.RestUser.persistence.model.UserEntity;
import net.digitallogic.RestUser.security.Role;
import net.digitallogic.RestUser.web.controller.request.CreateUserRequest;
import net.digitallogic.RestUser.web.dto.UserDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserMapperTest {

    UserMapper userMapper = new UserMapperImpl(new RoleMapperImpl(new AuthorityMapperImpl()));

    @Test
    public void createUserRequestToDtoTest() {
        CreateUserRequest createUserRequest = CreateUserRequest.builder()
                .firstName("Joe")
                .lastName("Exotic")
                .email("batshitcrazy@yahoo.com")
                .password("123456789")
                .build();

        UserDto userDto = userMapper.toDto(createUserRequest);

        assertThat(userDto).isEqualToComparingOnlyGivenFields(createUserRequest,
                "firstName", "lastName", "email", "password");
    }

    @Test
    public void userDtoToEntity() {
        UserDto userDto = UserFixtures.getDto();

        UserEntity userEntity = userMapper.toEntity(userDto);

        assertThat(userEntity).isEqualToComparingOnlyGivenFields(userDto,
                "firstName", "lastName", "email");

        assertThat(userEntity.getEncryptedPassword()).isNull();
        assertThat(userEntity.getRoles()).isEmpty();
    }

    @Test
    public void userEntityToDto() {
        UserEntity userEntity = UserFixtures.getEntity();
        UserDto userDto = userMapper.toDto(userEntity);

        assertThat(userDto).isEqualToComparingOnlyGivenFields(userEntity,
                "id", "firstName", "lastName","email");

        assertThat(userDto.getRoles()).isEmpty();
    }

    @Test
    public void userEntityToDtoWithRoles() {
        UserEntity userEntity = UserFixtures.getEntity();

        RoleFixtures.getRoleEntities(Role.ADMIN, Role.USER)
                .forEach(userEntity::addRole);

        assertThat(userEntity.getRoles()).isNotEmpty();
        UserDto userDto = userMapper.toDto(userEntity);

        assertThat(userDto.getRoles()).isNotEmpty();
    }
}
