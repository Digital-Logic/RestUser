package net.digitallogic.RestUser.persistence.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Entity(name = "RoleEntity")
@Table(name = "role")
public class RoleEntity extends AbstractBaseEntity{

    @Column(nullable = false, unique = true, length = 40)
    private String name;

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "role_authorities",
            joinColumns = @JoinColumn(name="role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name="authority_id", referencedColumnName = "id")
    )
    private Set<AuthorityEntity> authorities = new HashSet<>();

    public void addAuthority(AuthorityEntity authority) {
        authorities.add(authority);
    }
    public void removeAuthority(AuthorityEntity authority) {
        authorities.remove(authority);
    }
}
