package net.digitallogic.RestUser.service;

import lombok.extern.slf4j.Slf4j;
import net.digitallogic.RestUser.mapper.RoleMapper;
import net.digitallogic.RestUser.persistence.model.AuthorityEntity;
import net.digitallogic.RestUser.persistence.model.RoleEntity;
import net.digitallogic.RestUser.persistence.repository.AuthorityRepository;
import net.digitallogic.RestUser.persistence.repository.RoleRepository;
import net.digitallogic.RestUser.web.dto.AuthorityDto;
import net.digitallogic.RestUser.web.dto.RoleDto;
import net.digitallogic.RestUser.web.exceptions.BadRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class RoleService {

    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;
    private final RoleMapper roleMapper;
    private final MessageSource messageSource;

    @Autowired
    public RoleService(RoleRepository roleRepository, AuthorityRepository authorityRepository,
                       RoleMapper roleMapper, MessageSource messageSource) {
        this.roleRepository = roleRepository;
        this.authorityRepository = authorityRepository;
        this.roleMapper = roleMapper;
        this.messageSource = messageSource;
    }

    // === Get ROLE by ID === //
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public RoleDto getRole(UUID id) {
        Optional<RoleEntity> roleOptional = roleRepository.findById(id);
        if (roleOptional.isEmpty())
            throw new BadRequest(getMessage("exception.entityDoesNotExist", "Role", id));

        return roleMapper.toDto(
            roleOptional.get()
        );
    }

    // === Get All Roles === //
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<RoleDto> getRoles() {
        return roleMapper.toDto(
            roleRepository.findAll()
        );
    }

    // === Create New Role === //
    @Transactional
    public RoleDto createRole(RoleDto roleData) {
        if (roleRepository.existsByName(roleData.getName()))
            throw new BadRequest(getMessage("exception.duplicateEntity", "Role", roleData.getName()));

        RoleEntity roleEntity = RoleEntity.builder()
                .name(roleData.getName())
                .build();

        // Resolve role authorities to Authority Entity Set to insert into new RoleEntity
        Set<AuthorityEntity> authorities = StreamSupport.stream(
                authorityRepository.findAllById(
                    // convert Authority List into a list of Authority ids
                    roleData.getAuthorities().stream()
                        .map(AuthorityDto::getId)
                        .collect(Collectors.toList())
                ).spliterator(), false)
                .collect(Collectors.toSet());

        // add authorities to Role entity using the adder method
        authorities.forEach(roleEntity::addAuthority);

        // save entity to db
        return roleMapper.toDto(
            roleRepository.save(roleEntity)
        );
    }

    // === Update Role === //
    @Transactional
    public RoleDto updateRole(RoleDto updateInfo) {
        Optional<RoleEntity> roleOptional = roleRepository.findByIdWithAuthorities(updateInfo.getId());
        if (roleOptional.isEmpty())
            throw new BadRequest(getMessage("exception.entityDoesNotExist", "Role", updateInfo.getId()));

        RoleEntity role = roleOptional.get();

        if (updateInfo.getName() != null)
            role.setName(updateInfo.getName());

        Set<AuthorityEntity> updatedAuthorities = StreamSupport.stream(
            authorityRepository.findAllById(
                updateInfo.getAuthorities().stream()
                    .map(AuthorityDto::getId)
                    .collect(Collectors.toList()
                )
            ).spliterator(), false)
                .collect(Collectors.toSet());

        // set of authorities to add
        Set<AuthorityEntity> addSet = updatedAuthorities.stream()
                .filter(Predicate.not(role.getAuthorities()::contains))
                .collect(Collectors.toSet());

        // set of authorities to remove
        Set<AuthorityEntity> removeSet = role.getAuthorities().stream()
                .filter(updatedAuthorities::contains)
                .collect(Collectors.toSet());

        removeSet.forEach(role::removeAuthority);
        addSet.forEach(role::addAuthority);

        return roleMapper.toDto(role);
    }

    // === Delete Role === //
    @Transactional
    public void deleteRole(UUID id) {
        Optional<RoleEntity> roleOptional = roleRepository.findById(id);
        if (roleOptional.isEmpty())
            throw new BadRequest(getMessage("exception.entityDoesNotExist", "Role", id));

        RoleEntity role = roleOptional.get();

        // Remove each authority from the role
        role.getAuthorities().forEach(role::removeAuthority);

        roleRepository.delete(role);
    }

    private String getMessage(String code, Object... args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
