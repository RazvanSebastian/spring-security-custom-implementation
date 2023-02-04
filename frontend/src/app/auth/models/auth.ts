export interface LoginModel {
    principal: string;
    credentials: string;
}

export interface SocialAuthConsentUriModel {
    authUri: string;
}

export interface UserInfo {
    email: string;
    familyName?: string;
    givenName?: string;
    picture?: string;
}

export enum SocialAuthOption {
    GOOGLE = "google-auth",
    GITHUB = "github-auth"
}