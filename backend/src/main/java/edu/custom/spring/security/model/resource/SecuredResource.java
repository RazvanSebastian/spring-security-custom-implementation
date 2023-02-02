package edu.custom.spring.security.model.resource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.custom.spring.security.model.security.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "SecuredResource")
@Table(name = "secured_resource")
public class SecuredResource {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String value;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
