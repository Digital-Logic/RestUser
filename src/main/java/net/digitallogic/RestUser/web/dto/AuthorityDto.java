package net.digitallogic.RestUser.web.dto;

import lombok.*;

@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AuthorityDto {
    int id;
    private String name;
}
