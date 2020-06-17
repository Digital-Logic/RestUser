package net.digitallogic.RestUser.persistence.repository;

import net.digitallogic.RestUser.persistence.model.RoleEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends CrudRepository<RoleEntity, UUID> {

    Optional<RoleEntity> findByName(String name);
    Iterable<RoleEntity> findAllByNameIn(Iterable<String>roles);
    boolean existsByName(String name);

    @Query("SELECT role FROM RoleEntity  role " +
            "LEFT JOIN FETCH role.authorities authorities " +
            "WHERE role.id = :id")
    Optional<RoleEntity> findByIdWithAuthorities(@Param("id") UUID id);
}
