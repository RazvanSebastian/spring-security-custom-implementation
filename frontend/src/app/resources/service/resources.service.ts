import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SecuredResourceModel } from '../model';

@Injectable({
  providedIn: 'root'
})
export class ResourcesService {

  constructor(private httpClient: HttpClient) { }

  getUserResource() {
    return this.httpClient.get<SecuredResourceModel[]>('/api/resources/user');
  }

  getAllResources() {
    return this.httpClient.get<SecuredResourceModel[]>('/api/resources/user');
  }

  saveUserResource(value: string) {
    return this.httpClient.post<SecuredResourceModel>('/api/resources', value);
  }
}
