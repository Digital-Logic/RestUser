package net.digitallogic.RestUser.web.controller;

import lombok.extern.slf4j.Slf4j;
import net.digitallogic.RestUser.mapper.UserMapper;
import net.digitallogic.RestUser.service.UserService;
import net.digitallogic.RestUser.web.Routes;
import net.digitallogic.RestUser.web.controller.request.CreateUserRequest;
import net.digitallogic.RestUser.web.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = Routes.USER_API,
    produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
@Slf4j
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    // === REST METHODS === //

    // === GET METHODS === //
    @GetMapping(path = "/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUser(@PathVariable UUID uuid) {
        return userService.getUser(uuid);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getUsers(@RequestParam(value="page", defaultValue="0") int page,
                                  @RequestParam(value="limit", defaultValue="25") int limit) {

        return userService.getUsers(page, limit);
    }

    // === POST METHODS === //
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody CreateUserRequest createUser) {
        return userService.createUser(
                userMapper.toDto(createUser)
        );
    }

    // === PUT METHODS === //

    // === DELETE METHODS === //
}
