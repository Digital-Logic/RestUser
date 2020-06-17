package net.digitallogic.RestUser.mapper;


import net.digitallogic.RestUser.fixtures.RoleFixtures;
import net.digitallogic.RestUser.persistence.model.RoleEntity;
import net.digitallogic.RestUser.security.Role;
import net.digitallogic.RestUser.web.controller.request.CreateRoleRequest;
import net.digitallogic.RestUser.web.dto.RoleDto;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class RoleMapperTest {
    private RoleMapper roleMapper = new RoleMapperImpl(new AuthorityMapperImpl());

    @Test
    public void dtoToEntityTest() {
        RoleDto roleDto = RoleFixtures.getRoleDto(Role.ADMIN);
        RoleEntity roleEntity = roleMapper.toEntity(roleDto);

        assertThat(roleEntity).isEqualToComparingOnlyGivenFields(roleDto,
                "name");
        assertThat(roleEntity.getId()).isNotNull();
        assertThat(roleEntity.getAuthorities()).isEmpty();
    }

    @Test
    public void entityToDtoTest() {
        RoleEntity roleEntity = RoleFixtures.getRoleEntity(Role.ADMIN);
        RoleDto roleDto = roleMapper.toDto(roleEntity);
        assertThat(roleDto).isEqualToComparingOnlyGivenFields(roleEntity,
                "id", "name");

        assertThat(roleDto.getAuthorities()).isNotEmpty();
        assertThat(roleDto.getAuthorities()).hasSameSizeAs(roleEntity.getAuthorities());
    }

    @Test
    public void createToDtoTest() {
        CreateRoleRequest createRole = CreateRoleRequest.builder()
                .name("NEW_ROLE")
                .authorities(Arrays.asList(1,2,3,4))
                .build();

        RoleDto roleDto = roleMapper.toDto(createRole);

        assertThat(roleDto.getName()).isEqualTo(createRole.getName());
        assertThat(roleDto.getAuthorities()).hasSize(4);
    }
}
