package net.digitallogic.RestUser.mapper;

import net.digitallogic.RestUser.persistence.model.RoleEntity;
import net.digitallogic.RestUser.web.controller.request.CreateRoleRequest;
import net.digitallogic.RestUser.web.dto.RoleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.Persistence;
import javax.persistence.PersistenceUtil;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class RoleMapperImpl implements RoleMapper {

    private final AuthorityMapper authorityMapper;

    @Autowired
    public RoleMapperImpl(AuthorityMapper authorityMapper) {
        this.authorityMapper = authorityMapper;
    }

    @Override
    public RoleDto toDto(RoleEntity roleEntity) {
        RoleDto roleDto = RoleDto.builder()
                .id(roleEntity.getId())
                .name(roleEntity.getName())
                .build();

        PersistenceUtil pu = Persistence.getPersistenceUtil();

        if (pu.isLoaded(roleEntity.getAuthorities())) {
            roleDto.setAuthorities(
                    authorityMapper.toDto(roleEntity.getAuthorities())
            );
        }

        return roleDto;
    }

    @Override
    public RoleDto toDto(CreateRoleRequest roleRequest) {
        return RoleDto.builder()
                .name(roleRequest.getName())
                .authorities(authorityMapper.toDtoFromIds(roleRequest.getAuthorities()))
                .build();
    }

    @Override
    public RoleEntity toEntity(RoleDto roleDto) {
        RoleEntity roleEntity = RoleEntity.builder()
                .name(roleDto.getName())
                .build();
        if (roleDto.getId() != null)
            roleEntity.setId(roleDto.getId());

        return roleEntity;
    }

    @Override
    public List<RoleDto> toDto(Iterable<RoleEntity> roles) {
        return StreamSupport.stream(roles.spliterator(), false)
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoleEntity> toEntity(Iterable<RoleDto> roles) {
        return StreamSupport.stream(roles.spliterator(), false)
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
