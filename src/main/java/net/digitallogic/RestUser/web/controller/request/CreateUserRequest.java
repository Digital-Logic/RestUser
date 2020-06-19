package net.digitallogic.RestUser.web.controller.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
public class CreateUserRequest {

    @Size(max = 40, message = "{error.field.maxLength}")
    @NotNull(message = "{error.field.notNull}")
    private String firstName;

    @Size(max = 40, message = "{error.field.maxLength}")
    @NotNull(message = "{error.field.notNull}")
    private String lastName;

    @Size(max = 150, message = "{error.field.maxLength}")
    @NotNull(message = "{error.field.notNull}")
    private String email;

    @Size(min = 7, max = 60, message = "{error.field.minMaxLength}")
    @NotNull(message = "{error.field.notNull}")
    private String password;
}
