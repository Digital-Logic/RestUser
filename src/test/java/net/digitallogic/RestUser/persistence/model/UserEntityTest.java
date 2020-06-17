package net.digitallogic.RestUser.persistence.model;

import net.digitallogic.RestUser.fixtures.UserFixtures;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class UserEntityTest {

    @Test
    public void equalsAndHashCodeTest() {
        UserEntity user = UserFixtures.getEntity();
        UserEntity copy = UserEntity.builder()
                .id(user.getId())
                .build();

        assertThat(copy).isEqualTo(user);
        assertThat(copy).hasSameHashCodeAs(user);
    }

    @Test
    public void builderTest() {
        UserEntity user = UserEntity.builder()
                .build();

        assertThat(user.getId()).isNotNull();
    }

    @Test
    public void toBuilderTest() {
        UserEntity user = UserFixtures.getEntity();
        UserEntity copy = user.toBuilder().build();

        assertThat(copy).isEqualToComparingFieldByField(user);
        assertThat(copy).isEqualTo(user);

        copy.setId(UUID.randomUUID());
        assertThat(copy).isNotEqualTo(user);
    }
}
