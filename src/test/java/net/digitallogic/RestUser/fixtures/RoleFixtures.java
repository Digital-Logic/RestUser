package net.digitallogic.RestUser.fixtures;

import net.digitallogic.RestUser.mapper.AuthorityMapperImpl;
import net.digitallogic.RestUser.mapper.RoleMapper;
import net.digitallogic.RestUser.mapper.RoleMapperImpl;
import net.digitallogic.RestUser.persistence.model.RoleEntity;
import net.digitallogic.RestUser.security.Role;
import net.digitallogic.RestUser.web.dto.RoleDto;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RoleFixtures {

    private static RoleMapper roleMapper = new RoleMapperImpl(new AuthorityMapperImpl());

    private static Map<String, RoleEntity> roleData = Arrays.stream(new RoleEntity[]{
            RoleEntity.builder()
                .name(Role.ADMIN.name)
                .authorities(new HashSet<>(
                        // Get all authorities
                        AuthorityFixtures.getAuthorityEntities()
                ))
                .build(),
            RoleEntity.builder()
                .name(Role.USER.name)
                .build()
    }) // transform stream into HashMap
    .collect(Collectors.toMap(RoleEntity::getName, Function.identity()));


    // === ENTITY FIXTURES === //
    private static RoleEntity copyRole(RoleEntity entity) {
        return entity.toBuilder()
                .build();
    }
    public static RoleEntity getRoleEntity(String role) {
        return copyRole(roleData.get(role));
    }

    public static RoleEntity getRoleEntity(Role role) {
        return getRoleEntity(role.name);
    }

    public static List<RoleEntity> getRoleEntities() {
        return roleData.values().stream()
                .map(RoleFixtures::copyRole)
                .collect(Collectors.toList());
    }

    public static List<RoleEntity> getRoleEntities(Role... roles) {
        return Arrays.stream(roles)
                .map(Role::name)
                .map(roleData::get)
                .collect(Collectors.toList());
    }

    // === DTO FIXTURES === //
    public static RoleDto getRoleDto(String role) {
        return roleMapper.toDto(getRoleEntity(role));
    }
    public static RoleDto getRoleDto(Role role) {
        return getRoleDto(role.name);
    }
    public static List<RoleDto> getRoleDtos() {
        return roleData.values().stream()
                .map(roleMapper::toDto)
                .collect(Collectors.toList());
    }
}
