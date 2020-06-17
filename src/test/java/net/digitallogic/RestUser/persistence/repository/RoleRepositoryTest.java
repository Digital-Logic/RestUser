package net.digitallogic.RestUser.persistence.repository;

import net.digitallogic.RestUser.fixtures.RoleFixtures;
import net.digitallogic.RestUser.persistence.model.RoleEntity;
import net.digitallogic.RestUser.security.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUtil;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private EntityManager entityManager;

    private static final List<RoleEntity.RoleEntityBuilder> roleBuilders =
            RoleFixtures.getRoleEntities().stream()
            .map(RoleEntity::toBuilder)
            .map(builder -> builder.authorities(new HashSet<>()))
            .collect(Collectors.toList());

    @BeforeEach
    public void setup() {

        roleRepository.saveAll(
                roleBuilders.stream()
                .map(builder -> builder.build())
                .collect(Collectors.toList())
        );

        entityManager.flush();
        entityManager.clear();
        System.out.println("========== SETUP COMPLETE ===========");
    }

    @Test
    public void findByNameTest() {
        Optional<RoleEntity> roleOptional = roleRepository.findByName(roleBuilders.get(0).build().getName());
        assertThat(roleOptional).isNotEmpty();

        RoleEntity role = roleOptional.get();

        PersistenceUtil pu = Persistence.getPersistenceUtil();
        assertThat(pu.isLoaded(role.getAuthorities())).isFalse();
    }

    @Test
    public void findAllByNameInTest() {
        Iterable<RoleEntity> roleEntities = roleRepository.findAllByNameIn(
                roleBuilders.stream()
                    .map(builder -> builder.build())
                    .map(RoleEntity::getName)
                .collect(Collectors.toList())
        );

        assertThat(roleEntities).isNotEmpty();
        assertThat(roleEntities).hasSameSizeAs(roleBuilders);
        assertThat(roleEntities).containsAll(
                roleBuilders.stream()
                        .map(builder -> builder.build())
                    .collect(Collectors.toList())
        );
    }

    @Test
    public void existsByNameTest() {
        assertThat(roleRepository.existsByName(
                roleBuilders.get(0)
                        .build()
                        .getName())
        ).isTrue();
    }

    @Test
    public void findByIdWithAuthoritiesTest() {
        Optional<RoleEntity> roleOptional = roleRepository.findByIdWithAuthorities(
                RoleFixtures.getRoleDto(Role.ADMIN).getId()
        );

        assertThat(roleOptional).isNotEmpty();

        PersistenceUtil pu = Persistence.getPersistenceUtil();
        RoleEntity role = roleOptional.get();
        assertThat(pu.isLoaded(role.getAuthorities())).isTrue();

    }
}
