package edu.custom.spring.security.model.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Privilege implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Enumerated(EnumType.STRING)
    private PrivilegesEnum authority;

    @Override
    public String getAuthority() {
        return authority.getValue();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Privilege) {
            return this.authority.equals(((Privilege) obj).authority);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.authority.hashCode();
    }

    @Override
    public String toString() {
        return this.authority.getValue();
    }
}
