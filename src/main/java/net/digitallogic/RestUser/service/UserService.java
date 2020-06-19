package net.digitallogic.RestUser.service;

import lombok.extern.slf4j.Slf4j;
import net.digitallogic.RestUser.mapper.UserMapper;
import net.digitallogic.RestUser.persistence.model.RoleEntity;
import net.digitallogic.RestUser.persistence.model.UserEntity;
import net.digitallogic.RestUser.persistence.repository.RoleRepository;
import net.digitallogic.RestUser.persistence.repository.UserRepository;
import net.digitallogic.RestUser.security.Role;
import net.digitallogic.RestUser.web.dto.RoleDto;
import net.digitallogic.RestUser.web.dto.UserDto;
import net.digitallogic.RestUser.web.exceptions.BadRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final MessageSource messageSource;
    private final PasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository,
                       UserMapper userMapper,
                       MessageSource messageSource, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
        this.messageSource = messageSource;
        this.encoder = encoder;
    }


   // === Get One User == //
   @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public UserDto getUser(UUID id){
           Optional<UserEntity> userOptional = userRepository.findByIdWithRoles(id);
           if (userOptional.isEmpty())
               throw new BadRequest(getMessage("exception.entityDoesNotExist", "user", id));

           return userMapper.toDto(userOptional.get());
    }

    // === Get Multiply Users by ID === //
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<UserDto> getUsers(Iterable<UUID> ids) {
        return userMapper.toDto(
            userRepository.findAllById(ids)
        );
    }

    // === Get Multiply Users === //
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<UserDto> getUsers(int page, int limit) {
        Page<UserEntity> userPage = userRepository.findAll(
                PageRequest.of(page, limit,
                        Sort.by("lastName").descending()
                                .and(Sort.by("firstName").descending())
                ));

        return userMapper.toDto(
                userPage.getContent()
        );
    }

    // === Create User === //
    @Transactional
    public UserDto createUser(UserDto userInfo) {

        if (userRepository.existsByEmailIgnoreCase(userInfo.getEmail()))
            throw new BadRequest(getMessage("exception.duplicateUserAccount", userInfo.getEmail()));

        // Create User entity without roles
        UserEntity user = UserEntity.builder()
                .firstName(userInfo.getFirstName())
                .lastName(userInfo.getLastName())
                .email(userInfo.getEmail())
                .encryptedPassword(encoder.encode(userInfo.getPassword()))
                .build();

        // build roles list
        Set<RoleEntity> roles = StreamSupport.stream(
            roleRepository.findAllById(
                userInfo.getRoles().stream()
                .map(RoleDto::getId)
                .collect(Collectors.toList())
            ).spliterator(), false)
            .collect(Collectors.toSet());

        try {
            roles.add(roleRepository.findByName(Role.USER.name).get());
        } catch (NoSuchElementException ex) {
            log.error("ERROR adding USER role to new user account: {}", userInfo.getEmail());
        }

        // add each role to the user entity using the adder method
        roles.forEach(user::addRole);

        return userMapper.toDto(
            userRepository.save(user)
        );
    }

    // === Update User === //
    @Transactional
    public UserDto updateUser(UserDto updateInfo) {
        Optional<UserEntity> userOptional = userRepository.findByIdWithRoles(updateInfo.getId());
        if (userOptional.isEmpty())
            throw new BadRequest(getMessage("exception.entityDoesNotExist", "User", updateInfo.getId()));
        // update user entity with the provided update info
        UserEntity user = userMapper.updateEntity(userOptional.get(), updateInfo);

        // TODO Implement update UserRole logic

        // return updated user
        return userMapper.toDto(user);
    }

    // === Delete User === //
    @Transactional
    public void deleteUser(UUID id) {
        Optional<UserEntity> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty())
            throw new BadRequest(
                    getMessage("exception.entityDoesNotExist", "User", id));

        UserEntity user = userOptional.get();

        user.getRoles().forEach(user::removeRole);

        userRepository.delete(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> userOptional = userRepository.findByEmailIgnoreCaseWithRolesAndAuthorities(email);
        if (userOptional.isEmpty())
            throw new UsernameNotFoundException(email);

        return userOptional.get();
    }

    private String getMessage(String code, Object... args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
