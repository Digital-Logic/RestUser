package net.digitallogic.RestUser.persistence.repository;

import net.digitallogic.RestUser.persistence.model.AuthorityEntity;
import org.springframework.data.repository.CrudRepository;

public interface AuthorityRepository extends CrudRepository<AuthorityEntity, Integer> {
}
