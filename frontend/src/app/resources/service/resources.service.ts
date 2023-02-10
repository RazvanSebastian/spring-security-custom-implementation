import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UserInfo } from 'src/app/auth/models/auth';
import { ItemsPage, ItemsSearchQueries, SecuredResourceModel, UserInfoModel } from '../model';

@Injectable({
  providedIn: 'root'
})
export class ResourcesService {

  constructor(private httpClient: HttpClient) { }

  getAllResource(itemsSearchQueries: ItemsSearchQueries) {
    return this.httpClient.get<ItemsPage<SecuredResourceModel>>('/api/resources/all', {
      params: new HttpParams()
        .set("searchedUserName", itemsSearchQueries.searchedUserName)
        .set("searchedValue", itemsSearchQueries.searchedValue)
        .set("pageIndex", itemsSearchQueries.pageIndex)
        .set("pageSize", itemsSearchQueries.pageSize)
        .set("sortDirection", itemsSearchQueries.sortDirection)
    });
  }

  getUserResource() {
    return this.httpClient.get<SecuredResourceModel[]>('/api/resources/user');
  }

  saveUserResource(value: string) {
    return this.httpClient.post<SecuredResourceModel>('/api/resources', value);
  }

  deleteResource(id: number) {
    return this.httpClient.delete('/api/resources', { params: new HttpParams().set("id", id) });
  }

  getUsers(itemsSearchQueries: ItemsSearchQueries) {
    return this.httpClient.get<ItemsPage<UserInfoModel>>('/api/users', {
      params: new HttpParams()
        .set("searchedUserName", itemsSearchQueries.searchedUserName)
        .set("searchedValue", itemsSearchQueries.searchedValue)
        .set("pageIndex", itemsSearchQueries.pageIndex)
        .set("pageSize", itemsSearchQueries.pageSize)
        .set("sortDirection", itemsSearchQueries.sortDirection)
    })
  }
}
