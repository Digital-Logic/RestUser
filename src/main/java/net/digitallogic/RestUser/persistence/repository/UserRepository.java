package net.digitallogic.RestUser.persistence.repository;


import net.digitallogic.RestUser.persistence.model.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends PagingAndSortingRepository<UserEntity, UUID> {

    Optional<UserEntity> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);

    @Query("SELECT user FROM UserEntity user " +
            "LEFT JOIN FETCH user.roles roles " +
            "WHERE user.id = :id")
    Optional<UserEntity> findByIdWithRoles(@Param("id") UUID id);

    @Query("SELECT user FROM UserEntity  user " +
            "LEFT JOIN FETCH user.roles roles " +
            "LEFT JOIN FETCH roles.authorities auth " +
            "WHERE user.id = :id")
    Optional<UserEntity> findByIdWithRolesAndAuthorities(@Param("id") UUID id);
}
