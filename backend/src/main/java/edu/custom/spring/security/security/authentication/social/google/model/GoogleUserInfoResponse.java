package edu.custom.spring.security.security.authentication.social.google.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import edu.custom.spring.security.security.authentication.social.model.SocialUserInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleUserInfoResponse implements SocialUserInfo {

    private String id;

    private String email;

    @JsonAlias("verified_email")
    private Boolean verifiedEmail;

    private String name;

    @JsonAlias("given_name")
    private String givenName;

    @JsonAlias("family_name")
    private String familyName;

    private String picture;

    private String locale;

}
