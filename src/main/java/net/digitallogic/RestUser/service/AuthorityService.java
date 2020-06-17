package net.digitallogic.RestUser.service;

import lombok.extern.slf4j.Slf4j;
import net.digitallogic.RestUser.mapper.AuthorityMapper;
import net.digitallogic.RestUser.persistence.model.AuthorityEntity;
import net.digitallogic.RestUser.persistence.repository.AuthorityRepository;
import net.digitallogic.RestUser.web.dto.AuthorityDto;
import net.digitallogic.RestUser.web.exceptions.BadRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AuthorityService {

    private final AuthorityRepository authorityRepository;
    private final AuthorityMapper authorityMapper;
    private final MessageSource messageSource;

    @Autowired
    public AuthorityService(AuthorityRepository authorityRepository, AuthorityMapper authorityMapper,
                            MessageSource messageSource) {
        this.authorityRepository = authorityRepository;
        this.authorityMapper = authorityMapper;
        this.messageSource = messageSource;
    }

    // === Get authority by id === //
    public AuthorityDto getAuthority(int id) {
        Optional<AuthorityEntity> authorityOptional = authorityRepository.findById(id);
        if (authorityOptional.isEmpty())
            throw new BadRequest(getMessage("exception.entityDoesNotExist", "Authority", id));

        return authorityMapper.toDto(authorityOptional.get());
    }

    // === Get All Authorities === //
    public List<AuthorityDto> getAuthorities() {
        Iterable<AuthorityEntity> authorities = authorityRepository.findAll();

        return authorityMapper.toDto(authorities);
    }

    private String getMessage(String code, Object... args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
