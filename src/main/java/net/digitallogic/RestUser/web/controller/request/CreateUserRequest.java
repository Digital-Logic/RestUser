package net.digitallogic.RestUser.web.controller.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
public class CreateUserRequest {

    @Size(max = 40, message = "${field.error.maxLength}")
    @NotNull(message = "${field.error.notNull}")
    private String firstName;

    @Size(max = 40, message = "${field.error.maxLength}")
    @NotNull(message = "${field.error.notNull}")
    private String lastName;

    @Size(max = 150, message = "${field.error.maxLength}")
    @NotNull(message = "${field.error.notNull}")
    private String email;

    @Size(min = 7, max = 60, message = "${field.error.minMaxLength}")
    @NotNull(message = "${field.error.notNull}")
    private String password;
}
