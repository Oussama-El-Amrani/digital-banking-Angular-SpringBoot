import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BankAccount, BankAccountDTO, AccountType } from '../models/bank-account.model';
import { environment } from '../../environments/environment';

interface BankAccountRequestDTO {
  balance: number;
  type: AccountType;
  customerId: number;
  interestRate?: number; // for savings accounts
  overDraft?: number; // for current accounts
}

interface TransactionRequest {
  accountId: string;
  amount: number;
  description: string;
}

interface TransferRequest {
  sourceAccountId: string;
  destinationAccountId: string;
  amount: number;
  description: string;
}

@Injectable({
  providedIn: 'root'
})
export class BankAccountService {
  private apiUrl = `${environment.apiUrl}/accounts`;

  constructor(private http: HttpClient) { }

  getAllAccounts(): Observable<BankAccountDTO[]> {
    return this.http.get<BankAccountDTO[]>(`${this.apiUrl}/`);
  }

  getAccountById(id: string): Observable<BankAccountDTO> {
    return this.http.get<BankAccountDTO>(`${this.apiUrl}/${id}`);
  }

  getAccountsByCustomerId(customerId: number): Observable<BankAccountDTO[]> {
    // This is using the correct endpoint as per the API documentation
    return this.http.get<BankAccountDTO[]>(`${environment.apiUrl}/customers/${customerId}/accounts`);
  }

  createSavingAccount(customerId: number, initialBalance: number, interestRate: number): Observable<BankAccountDTO> {
    const request = {
      customerId,
      initialBalance,
      interestRate
    };
    return this.http.post<BankAccountDTO>(`${this.apiUrl}/saving`, request);
  }

  createCurrentAccount(customerId: number, initialBalance: number, overdraftLimit: number): Observable<BankAccountDTO> {
    const request = {
      customerId,
      initialBalance,
      overdraftLimit
    };
    return this.http.post<BankAccountDTO>(`${this.apiUrl}/current`, request);
  }

  debit(accountId: string, amount: number, description: string): Observable<any> {
    const request: TransactionRequest = {
      accountId,
      amount,
      description
    };
    return this.http.post(`${this.apiUrl}/withdraw`, request);
  }

  credit(accountId: string, amount: number, description: string): Observable<any> {
    const request: TransactionRequest = {
      accountId,
      amount,
      description
    };
    return this.http.post(`${this.apiUrl}/deposit`, request);
  }

  transfer(sourceAccountId: string, destinationAccountId: string, amount: number, description: string): Observable<any> {
    const request: TransferRequest = {
      sourceAccountId,
      destinationAccountId,
      amount,
      description
    };
    return this.http.post(`${this.apiUrl}/transfer`, request);
  }
}
