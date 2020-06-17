package net.digitallogic.RestUser.mapper;

import net.digitallogic.RestUser.persistence.model.RoleEntity;
import net.digitallogic.RestUser.web.controller.request.CreateRoleRequest;
import net.digitallogic.RestUser.web.dto.RoleDto;

import java.util.List;


public interface RoleMapper {
    RoleDto toDto(RoleEntity role);
    RoleDto toDto(CreateRoleRequest roleRequest);
    RoleEntity toEntity(RoleDto role);
    List<RoleDto> toDto(Iterable<RoleEntity> roles);
    List<RoleEntity> toEntity(Iterable<RoleDto> roles);
}
