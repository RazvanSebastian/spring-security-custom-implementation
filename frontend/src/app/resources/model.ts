export interface SecuredResourceModel {
    id: number;
    value: string;
    audit: AuditResourceModel;
}

export interface AuditResourceModel {
    createdOn: Date;
    createdBy: string;
    updatedOn: Date;
    updatedBy: string;
}

export enum Sort {
    ASC = "ASC",
    DESC = "DESC"
}

export interface ItemsSearchQueries {
    pageSize: number;
    pageIndex: number;
    sortDirection: Sort;
    searchedValue: string;
    searchedUserName: string;
}

export interface ItemsPage<T> {
    content: T[];
    totalElements: number;
    totalPages: number;
    currentPage: number;
}

export interface UserInfoModel {
    id: number;
    username: string;
    givenName: string;
}