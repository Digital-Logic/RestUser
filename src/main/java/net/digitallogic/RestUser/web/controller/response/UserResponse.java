package net.digitallogic.RestUser.web.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;
import java.util.UUID;

@Data
@EqualsAndHashCode(of = "id")
@Builder(toBuilder = true)
@JacksonXmlRootElement(localName = "User")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserResponse {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private Set<String> authorities;
}
