package net.digitallogic.RestUser.persistence.model;

import net.digitallogic.RestUser.fixtures.RoleFixtures;
import net.digitallogic.RestUser.security.Role;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RoleEntityTest {

    @Test
    public void equalsAndHashTest() {
        RoleEntity entity = RoleFixtures.getRoleEntity(Role.ADMIN);
        RoleEntity copy = RoleEntity.builder()
                .id(entity.getId())
                .build();

        assertThat(copy).isEqualTo(entity);
        assertThat(copy).hasSameHashCodeAs(entity);
    }

    @Test
    public void builderTest() {
        RoleEntity entity = RoleEntity.builder().build();
        assertThat(entity.getId()).isNotNull();
        assertThat(entity.getAuthorities()).isEmpty();
    }

    @Test
    public void toBuilderTest() {
        RoleEntity adminRole = RoleFixtures.getRoleEntity(Role.ADMIN);
        RoleEntity copy  = adminRole.toBuilder().build();

        assertThat(copy).isEqualToComparingFieldByField(adminRole);
    }
}
