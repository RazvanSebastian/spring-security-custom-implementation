package edu.custom.spring.security.model.entity.resource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.custom.spring.security.model.entity.Audit;
import edu.custom.spring.security.model.entity.BaseEntity;
import edu.custom.spring.security.model.entity.security.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "SecuredResource")
@Table(name = "secured_resource")
public class SecuredResource extends BaseEntity {

    private String value;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Embedded
    private Audit audit = new Audit();

}
