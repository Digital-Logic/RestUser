package net.digitallogic.RestUser.persistence.model;

import net.digitallogic.RestUser.fixtures.AuthorityFixtures;
import net.digitallogic.RestUser.security.Authority;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthorityEntityTest {

    @Test
    public void equalsAndHashCodeTest() {

        AuthorityEntity entity = AuthorityFixtures.getAuthorityEntity(Authority.CREATE_USER);
        AuthorityEntity copy = AuthorityEntity.builder().id(entity.getId()).build();

        assertThat(copy).isEqualTo(entity);
        assertThat(copy).hasSameHashCodeAs(entity);
    }

    @Test
    public void builderTest() {
        AuthorityEntity entity = AuthorityEntity.builder()
                .name("NEW_AUTHORITY")
                .build();
        assertThat(entity.getId()).isNotNull();
    }

    @Test
    public void toBuilderTest() {
        AuthorityEntity entity = AuthorityFixtures.getAuthorityEntity(Authority.CREATE_USER);
        AuthorityEntity copy = entity.toBuilder().build();

        assertThat(copy).isEqualToComparingFieldByField(entity);
        assertThat(copy).hasSameHashCodeAs(entity);
        assertThat(copy).isEqualTo(entity);

        copy.setId(new Random().nextInt());
        assertThat(copy).isNotEqualTo(entity);
    }
}
