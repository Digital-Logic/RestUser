package net.digitallogic.RestUser.fixtures;

import net.digitallogic.RestUser.mapper.AuthorityMapper;
import net.digitallogic.RestUser.mapper.AuthorityMapperImpl;
import net.digitallogic.RestUser.persistence.model.AuthorityEntity;
import net.digitallogic.RestUser.security.Authority;
import net.digitallogic.RestUser.web.dto.AuthorityDto;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AuthorityFixtures {
    private static int index = 0;
    private static Random random = new Random();
    private static AuthorityMapper authorityMapper = new AuthorityMapperImpl();

    private static Map<String, AuthorityEntity> authorityData =
            Arrays.stream(Authority.values())
                .map(auth -> AuthorityEntity.builder()
                    .authority(auth.name)
                    .id(index++)
                    .build()
                )
            .collect(Collectors.toMap(AuthorityEntity::getAuthority, Function.identity()));

    private static AuthorityEntity copy(AuthorityEntity authorityEntity) {
        return authorityEntity.toBuilder()
                .build();
    }

    // return one single, randomly selected authority
    public static AuthorityEntity getAuthorityEntity(String authority) {
        return copy(authorityData.get(authority));
    }
    public static AuthorityEntity getAuthorityEntity(Authority authority) {
        return getAuthorityEntity(authority.name);
    }
    public static List<AuthorityEntity> getAuthorityEntities(Authority ...authorities) {
        return Arrays.stream(authorities)
                .map(AuthorityFixtures::getAuthorityEntity)
                .collect(Collectors.toList());
    }
    public static List<AuthorityEntity> getAuthorityEntities() {
        return authorityData.values().stream()
                .map(AuthorityFixtures::copy)
                .collect(Collectors.toList());
    }


    public static AuthorityDto getAuthorityDto(String authority) {
        return authorityMapper.toDto(authorityData.get(authority));
    }
    public static AuthorityDto getAuthorityDto(Authority authority) {
        return getAuthorityDto(authority.name);
    }
    public static List<AuthorityDto> getAuthorityDtos(Authority... authorities) {
        return Arrays.stream(authorities)
                .map(AuthorityFixtures::getAuthorityDto)
                .collect(Collectors.toList());
    }
    public static List<AuthorityDto> getAuthorityDtos() {
        return authorityData.values().stream()
                .map(authorityMapper::toDto)
                .collect(Collectors.toList());
    }
}
