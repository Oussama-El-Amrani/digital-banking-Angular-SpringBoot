import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AccountOperation, AccountHistoryDTO } from '../models/account-operation.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AccountOperationService {
  private apiUrl = `${environment.apiUrl}/operations`;

  constructor(private http: HttpClient) { }

  getOperationsByAccountId(accountId: string, page: number = 0, size: number = 10): Observable<AccountHistoryDTO> {
    return this.http.get<AccountHistoryDTO>(`${this.apiUrl}/account/${accountId}?page=${page}&size=${size}`);
  }

  getOperationById(id: number): Observable<AccountOperation> {
    return this.http.get<AccountOperation>(`${this.apiUrl}/${id}`);
  }
}
