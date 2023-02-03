export interface LoginModel {
    principal: string;
    credentials: string;
}

export interface GoogleAuthConsentUriResponseModel {
    uri: string;
}

export interface UserInfo {
    email: string;
    familyName?: string;
    givenName?: string;
    picture?: string;
}