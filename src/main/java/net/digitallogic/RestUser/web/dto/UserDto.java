package net.digitallogic.RestUser.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@JacksonXmlRootElement(localName = "User")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    @Builder.Default
    private List<RoleDto> roles = new ArrayList<>();
}
