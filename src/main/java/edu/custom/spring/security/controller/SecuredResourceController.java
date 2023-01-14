package edu.custom.spring.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/resource")
public class SecuredResourceController {

    @GetMapping
    public ResponseEntity<?> getSecuredResource() {
        return ResponseEntity.ok("Secured resource");
    }

    @PostMapping
    public ResponseEntity<?> postSecuredResource(@RequestBody String body) {
        return ResponseEntity.ok(body);
    }
}
