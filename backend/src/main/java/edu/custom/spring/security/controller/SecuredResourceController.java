package edu.custom.spring.security.controller;

import edu.custom.spring.security.service.resource.SecuredResourceService;
import edu.custom.spring.security.service.security.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;


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
    public ResponseEntity getAllSecuredResources() {
        return ResponseEntity.ok(securedResourceService.getAllSecuredResources());
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
    public ResponseEntity getSecuredResource(@RequestParam("id") Long id) {
        securedResourceService.delete(id);
        return ResponseEntity.ok().build();
    }
}
