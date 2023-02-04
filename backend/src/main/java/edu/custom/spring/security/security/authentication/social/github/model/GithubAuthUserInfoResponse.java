package edu.custom.spring.security.security.authentication.social.github.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import edu.custom.spring.security.model.security.AuthenticationType;
import edu.custom.spring.security.security.authentication.social.base.model.SocialAuthUserInfoResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class GithubAuthUserInfoResponse implements SocialAuthUserInfoResponse {

    private String id;

    private String email;

    private String name;

    @JsonAlias("avatar_url")
    private String picture;

    public String getGivenName() {
        if (Objects.nonNull(name)) {
            final String[] userName = name.split(" ");
            return userName[0];
        }
        return "";
    }

    public String getFamilyName() {
        if (Objects.nonNull(name)) {
            final String[] userName = name.split(" ");
            return userName.length == 2 ? userName[1] : "";
        }
        return "";
    }

    @Override
    public AuthenticationType getAuthenticationType() {
        return AuthenticationType.GITHUB;
    }
}
