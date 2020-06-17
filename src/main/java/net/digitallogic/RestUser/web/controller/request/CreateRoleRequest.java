package net.digitallogic.RestUser.web.controller.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CreateRoleRequest {
    private String name;
    private List<Integer> authorities;
}
