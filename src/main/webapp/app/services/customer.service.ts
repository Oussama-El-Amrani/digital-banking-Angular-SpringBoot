import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {catchError, Observable, throwError} from 'rxjs';
import {Customer} from '../models/customer.model';
import {environment} from '../../environments/environment';

interface CustomerRequestDTO {
  name: string;
  email: string;
}

interface CustomerWithAccountsDTO extends Customer {
  bankAccountIds: string[];
}

@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  private apiUrl = `${environment.apiUrl}/customers`;

  constructor(private http: HttpClient) { }

  getAllCustomers(): Observable<Customer[]> {
    return this.http.get<Customer[]>(`${this.apiUrl}/`)
      .pipe(
        catchError(this.handleError)
      );
  }

  getAllCustomersWithAccounts(): Observable<CustomerWithAccountsDTO[]> {
    return this.http.get<CustomerWithAccountsDTO[]>(`${this.apiUrl}/?includeAccounts=true`)
      .pipe(
        catchError(this.handleError)
      );
  }

  getCustomerById(id: number): Observable<Customer> {
    return this.http.get<Customer>(`${this.apiUrl}/${id}`)
      .pipe(
        catchError(this.handleError)
      );
  }

  getCustomerWithAccounts(id: number): Observable<CustomerWithAccountsDTO> {
    return this.http.get<CustomerWithAccountsDTO>(`${this.apiUrl}/${id}?includeAccounts=true`)
      .pipe(
        catchError(this.handleError)
      );
  }

  createCustomer(customer: CustomerRequestDTO): Observable<Customer> {
    return this.http.post<Customer>(this.apiUrl, customer)
      .pipe(
        catchError(this.handleError)
      );
  }

  updateCustomer(id: number, customer: CustomerRequestDTO): Observable<Customer> {
    return this.http.put<Customer>(`${this.apiUrl}/${id}`, customer)
      .pipe(
        catchError(this.handleError)
      );
  }

  deleteCustomer(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`)
      .pipe(
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
    }
    console.error(errorMessage);
    return throwError(() => new Error(errorMessage));
  }
}
