import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { BankAccountDTO } from '../models/bank-account.model';

@Injectable({
  providedIn: 'root'
})
export class CustomerAccountService {
  private apiUrl = `${environment.apiUrl}/customer/accounts`;

  constructor(private http: HttpClient) { }

  getMyAccounts(): Observable<BankAccountDTO[]> {
    return this.http.get<BankAccountDTO[]>(this.apiUrl);
  }

  getMyAccount(id: string): Observable<BankAccountDTO> {
    return this.http.get<BankAccountDTO>(`${this.apiUrl}/${id}`);
  }

  deposit(accountId: string, amount: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/deposit`, {
      accountId,
      amount
    });
  }

  withdraw(accountId: string, amount: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/withdraw`, {
      accountId,
      amount
    });
  }

  transfer(sourceAccountId: string, destinationAccountId: string, amount: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/transfer`, {
      sourceAccountId,
      destinationAccountId,
      amount
    });
  }
}
