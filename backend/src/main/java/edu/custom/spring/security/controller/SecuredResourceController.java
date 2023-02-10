package edu.custom.spring.security.controller;

import edu.custom.spring.security.service.resource.SecuredResourceService;
import edu.custom.spring.security.service.security.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.Optional;


@RestController
@RequestMapping("/resources")
public class SecuredResourceController {

    private final SecuredResourceService securedResourceService;
    private final UserService userService;

    public SecuredResourceController(SecuredResourceService securedResourceService, UserService userService) {
        this.securedResourceService = securedResourceService;
        this.userService = userService;
    }

    @RolesAllowed("ADMIN_READ")
    @GetMapping("/all")
    public ResponseEntity getAllSecuredResources(
            @RequestParam(required = false) Optional<String> searchedUserName,
            @RequestParam(required = false) Optional<String> searchedValue,
            @RequestParam(required = false) Optional<Integer> pageIndex,
            @RequestParam(required = false) Optional<Integer> pageSize,
            @RequestParam(required = false) Optional<Sort.Direction> sortDirection
    ) {
        return ResponseEntity.ok(securedResourceService.getAllSecuredResources(
                searchedUserName.orElse(""),
                searchedValue.orElse(""),
                pageIndex.filter(value -> value >= 0).orElse(0),
                pageSize.filter(value -> value >= 0).orElse(10),
                sortDirection.orElse(Sort.DEFAULT_DIRECTION)));
    }

    @GetMapping("/user")
    public ResponseEntity getUserSecuredResources() {
        return ResponseEntity.ok(securedResourceService.getAllSecuredResources(userService.getAuthenticatedUser()));
    }

    @PostMapping
    public ResponseEntity postSecuredResource(@RequestBody String body) {
        return ResponseEntity.ok(securedResourceService.save(body, userService.getAuthenticatedUser()));
    }

    @RolesAllowed("ADMIN_WRITE")
    @DeleteMapping
    public ResponseEntity getSecuredResource(@RequestParam(name = "id") Long id) {
        securedResourceService.delete(id);
        return ResponseEntity.ok().build();
    }
}
