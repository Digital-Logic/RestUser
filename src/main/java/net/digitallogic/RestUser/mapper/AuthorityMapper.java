package net.digitallogic.RestUser.mapper;

import net.digitallogic.RestUser.persistence.model.AuthorityEntity;
import net.digitallogic.RestUser.web.dto.AuthorityDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = config.class)
public interface AuthorityMapper {

    AuthorityDto toDto(AuthorityEntity authority);

    default AuthorityDto toDto(int id) {
        return AuthorityDto.builder()
                .id(id)
                .build();
    }

    AuthorityEntity toEntity(AuthorityDto authority);
    List<AuthorityDto> toDtoFromIds(Iterable<Integer> authorityIds);
    List<AuthorityDto> toDto(Iterable<AuthorityEntity> authorities);
    List<AuthorityEntity> toEntity(Iterable<AuthorityDto> authorities);
}
