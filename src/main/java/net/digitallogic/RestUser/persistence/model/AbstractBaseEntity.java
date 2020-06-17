package net.digitallogic.RestUser.persistence.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.util.UUID;

@Data
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@MappedSuperclass
public abstract class AbstractBaseEntity implements Persistable<UUID> {

    @Id
    @Column(name = "id", unique = true, nullable = false, length = 16)
    @Builder.Default
    private UUID id = UUID.randomUUID();

    @Setter(AccessLevel.NONE)
    @Version
    @Column(name = "optlock", columnDefinition = "integer DEFAULT 0", nullable = false)
    protected int version;

    @Builder.Default
    @Transient
    @Setter(AccessLevel.NONE)
    private boolean isNew = true;

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PostPersist
    @PostLoad
    void toggleIsNew() { isNew = false; }
}
