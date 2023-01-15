import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ResourcesService {

  constructor(private httpClient: HttpClient) { }

  getResource() {
    return this.httpClient.get('/api/resource', { responseType: 'text' });
  }

  postResource(value: string) {
    return this.httpClient.post('/api/resource', value, { responseType: 'text' });
  }
}
