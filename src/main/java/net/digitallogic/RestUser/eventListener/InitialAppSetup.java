package net.digitallogic.RestUser.eventListener;

import lombok.extern.slf4j.Slf4j;
import net.digitallogic.RestUser.persistence.model.AuthorityEntity;
import net.digitallogic.RestUser.persistence.model.RoleEntity;
import net.digitallogic.RestUser.persistence.model.UserEntity;
import net.digitallogic.RestUser.persistence.repository.AuthorityRepository;
import net.digitallogic.RestUser.persistence.repository.RoleRepository;
import net.digitallogic.RestUser.persistence.repository.UserRepository;
import net.digitallogic.RestUser.security.Authority;
import net.digitallogic.RestUser.security.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static net.digitallogic.RestUser.security.Role.ADMIN;

@Slf4j
@Component
public class InitialAppSetup {

    private final AuthorityRepository authorityRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public InitialAppSetup(AuthorityRepository authorityRepository, RoleRepository roleRepository,
                           UserRepository userRepository, PasswordEncoder encoder) {
        this.authorityRepository = authorityRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @EventListener
    @Transactional
    public void onApplicationReadyEvent(ApplicationReadyEvent onReadyEvent) {
        log.info("Initializing Application Data.");

        /*
        * Setup Authority Entities
        * */
        log.info("Setup Authorities");
        Map<String, AuthorityEntity> authorities = StreamSupport.stream(
                authorityRepository.findAll().spliterator(), false
        ).collect(Collectors.toMap(AuthorityEntity::getName, Function.identity()));

        List<AuthorityEntity> missingAuthorities =
                Arrays.stream(Authority.values())
                .map(Authority::name)
                .filter(Predicate.not(authorities::containsKey))
                .map(auth -> AuthorityEntity.builder()
                        .name(auth)
                        .build()
                )
                .collect(Collectors.toList());

        if (!missingAuthorities.isEmpty()) {
            try {
                authorityRepository.saveAll(missingAuthorities);
            } catch (IllegalArgumentException ex) {
                log.error("ERROR creating authorities, exception thrown {}", ex);
            }
        }

        /*
        * Setup Default Roles
        **/
        log.info("Setup Default Roles");
        Map<String, RoleEntity> roles = StreamSupport.stream(
                roleRepository.findAll().spliterator(), false
        ).collect(Collectors.toMap(RoleEntity::getName, Function.identity()));

        List<RoleEntity> missingRoles =
                Arrays.stream(Role.values())
                .map(Role::name)
                .filter(Predicate.not(roles::containsKey))
                .map(roleName -> {
                    if (roleName == ADMIN.name) {
                        return RoleEntity.builder()
                                .name(roleName)
                                .authorities(
                                        StreamSupport.stream(
                                            authorityRepository.findAll().spliterator(), false)
                                        .collect(Collectors.toSet())
                                )
                                .build();
                    } else {
                        return RoleEntity.builder()
                                .name(roleName)
                                .build();
                    }
                })
                .collect(Collectors.toList());

        if(!missingRoles.isEmpty()) {
            try {
                roleRepository.saveAll(missingRoles);
            } catch (IllegalArgumentException ex) {
                log.error("ERROR creating roles: exception thrown: {}", ex);
            }
        }

        /*
        * Setup Default User Account
        * */
        log.info("Setup Default User Account");
        if(userRepository.count() == 0) {
            // create default user
            UserEntity adminUser = UserEntity.builder()
                    .firstName("Admin")
                    .lastName("Account")
                    .email("Admin@localhost")
                    .roles(StreamSupport.stream(
                                roleRepository.findAllByNameIn(
                                    Arrays.asList(ADMIN.name, Role.USER.name)
                                ).spliterator(),
                        false
                            )
                            .collect(Collectors.toSet()))
                    .encryptedPassword(encoder.encode("adminPassword"))
                    .build();

            userRepository.save(adminUser);
        }
    }
}
