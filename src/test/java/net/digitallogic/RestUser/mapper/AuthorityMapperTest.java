package net.digitallogic.RestUser.mapper;

import net.digitallogic.RestUser.web.dto.AuthorityDto;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthorityMapperTest {
    private AuthorityMapper authorityMapper = new AuthorityMapperImpl();

    @Test
    public void intToAuthorityTest() {
        AuthorityDto authorityDto = authorityMapper.toDto(1);
        assertThat(authorityDto.getId()).isEqualTo(1);
    }

    @Test
    public void intListToAuthorityTest() {
        List<AuthorityDto> authorities = authorityMapper.toDtoFromIds(Arrays.asList(1,2,3,4));
        assertThat(authorities).hasSize(4);
    }
}
