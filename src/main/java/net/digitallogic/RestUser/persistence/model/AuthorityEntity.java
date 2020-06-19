package net.digitallogic.RestUser.persistence.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity(name = "AuthorityEntity")
@Table(name = "authority")
public class AuthorityEntity implements GrantedAuthority {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(nullable = false, length = 40, unique = true)
    private String authority;

    @Override
    public String getAuthority() {
        return authority;
    }
}
