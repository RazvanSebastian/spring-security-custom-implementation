package edu.custom.spring.security.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.Embeddable;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Getter
@Setter
@Embeddable
public class Audit {

    private LocalDateTime createdOn;
    private String createdBy;
    private LocalDateTime updatedOn;
    private String updatedBy;

    @PrePersist
    public void prePersist() {
        this.createdOn = LocalDateTime.now();
        this.createdBy = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedOn = LocalDateTime.now();
        this.updatedBy = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
