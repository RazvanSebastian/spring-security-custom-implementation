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