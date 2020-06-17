package net.digitallogic.RestUser.persistence.repository;

import net.digitallogic.RestUser.fixtures.AuthorityFixtures;
import net.digitallogic.RestUser.fixtures.UserFixtures;
import net.digitallogic.RestUser.persistence.model.AuthorityEntity;
import net.digitallogic.RestUser.persistence.model.RoleEntity;
import net.digitallogic.RestUser.persistence.model.UserEntity;
import net.digitallogic.RestUser.security.Authority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUtil;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private EntityManager entityManager;

    private static final UserEntity.UserEntityBuilder userBuilder = UserFixtures.getEntity().toBuilder();

    @BeforeEach
    public void setup() {

        Set<AuthorityEntity> authorities = StreamSupport.stream(authorityRepository.saveAll(
                AuthorityFixtures.getAuthorityEntities(
                        Authority.CREATE_USER,
                        Authority.DELETE_USER
        )).spliterator(), false).collect(Collectors.toSet());

        RoleEntity role = roleRepository.save(
                RoleEntity.builder()
                        .name("ADMIN")
                        .authorities(authorities).build()
        );

        UserEntity user = userBuilder
                .roles(new HashSet<>(Arrays.asList(role)))
                .build();

        userRepository.save(user);
        entityManager.flush();
        entityManager.clear();

        System.out.println("User ID: " + userBuilder.build().getId());
        System.out.println("========== SETUP COMPLETED =============");
    }



    @Test
    public void findUserByEmailTest() {
        Optional<UserEntity> userOptional = userRepository.findByEmailIgnoreCase(userBuilder.build().getEmail());
        assertThat(userOptional).isNotEmpty();
    }

    @Test
    public void existByEmailIgnoreCaseTest() {
        assertThat(userRepository.existsByEmailIgnoreCase(userBuilder.build().getEmail())).isTrue();
    }

    @Test
    public void doesNotExistByEmailTest() {
        assertThat(userRepository.existsByEmailIgnoreCase("alskdfjo2ijla")).isFalse();
    }

    @Test
    public void findByIdTest() {
        Optional<UserEntity> userOptional = userRepository.findById(userBuilder.build().getId());
        assertThat(userOptional).isNotEmpty();
    }

    @Test
    public void findByIdWithRoles() {
        Optional<UserEntity> userOptional = userRepository.findByIdWithRoles(userBuilder.build().getId());
        assertThat(userOptional).isNotEmpty();

        UserEntity user = userOptional.get();
        assertThat(user.getRoles()).isNotEmpty();
        assertThat(user.getRoles().size()).isEqualTo(1);

        RoleEntity role = user.getRoles().iterator().next();
        PersistenceUtil pu = Persistence.getPersistenceUtil();
        assertThat(pu.isLoaded(role.getAuthorities())).isFalse();
    }

    @Test
    public void findByIdWithRolesAndAuthorities() {
        Optional<UserEntity> userOptional = userRepository.findByIdWithRolesAndAuthorities(userBuilder.build().getId());
        assertThat(userOptional).isNotEmpty();
        UserEntity user = userOptional.get();

        RoleEntity role = user.getRoles().iterator().next();
        PersistenceUtil pu = Persistence.getPersistenceUtil();
        assertThat(pu.isLoaded(role.getAuthorities())).isTrue();
        assertThat(role.getAuthorities().size()).isEqualTo(2);
    }

}
