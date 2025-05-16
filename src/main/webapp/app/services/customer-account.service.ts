import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {catchError, Observable, throwError} from 'rxjs';
import {environment} from '../../environments/environment';
import {BankAccountDTO} from '../models/bank-account.model';

@Injectable({
  providedIn: 'root'
})
export class CustomerAccountService {
  private apiUrl = `${environment.apiUrl}/customer/accounts`;

  constructor(private http: HttpClient) { }

  getMyAccounts(): Observable<BankAccountDTO[]> {
    return this.http.get<BankAccountDTO[]>(this.apiUrl)
      .pipe(
        catchError(this.handleError)
      );
  }

  getMyAccount(id: string): Observable<BankAccountDTO> {
    return this.http.get<BankAccountDTO>(`${this.apiUrl}/${id}`)
      .pipe(
        catchError(this.handleError)
      );
  }

  deposit(accountId: string, amount: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/deposit`, {
      accountId,
      amount
    }).pipe(
      catchError(this.handleError)
    );
  }

  withdraw(accountId: string, amount: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/withdraw`, {
      accountId,
      amount
    }).pipe(
      catchError(this.handleError)
    );
  }

  transfer(sourceAccountId: string, destinationAccountId: string, amount: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/transfer`, {
      sourceAccountId,
      destinationAccountId,
      amount
    }).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'An unknown error occurred';
    if (error.error instanceof ErrorEvent) {
      // Client-side error
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // Server-side error
      errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;

      // Pass through the status code so we can handle it in the component
      return throwError(() => error);
    }
    console.error(errorMessage);
    return throwError(() => new Error(errorMessage));
  }
}
