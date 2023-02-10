package edu.custom.spring.security.controller;

import edu.custom.spring.security.service.security.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @RolesAllowed("ADMIN_READ")
    @GetMapping
    public ResponseEntity getUsers(
            @RequestParam(required = false) Optional<String> searchedUserName,
            @RequestParam(required = false) Optional<Integer> pageIndex,
            @RequestParam(required = false) Optional<Integer> pageSize,
            @RequestParam(required = false) Optional<Sort.Direction> sortDirection
    ) {
        return ResponseEntity.ok(this.userService.getUsers(
                searchedUserName.orElse(""),
                pageIndex.filter(value -> value >= 0).orElse(0),
                pageSize.filter(value -> value >= 0).orElse(10),
                sortDirection.orElse(Sort.DEFAULT_DIRECTION))
        );
    }

}
