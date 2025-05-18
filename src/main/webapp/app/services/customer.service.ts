import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Customer } from '../models/customer.model';
import { environment } from '../../environments/environment';

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
    return this.http.get<Customer[]>(`${this.apiUrl}/`);
  }

  getAllCustomersWithAccounts(): Observable<CustomerWithAccountsDTO[]> {
    return this.http.get<CustomerWithAccountsDTO[]>(`${this.apiUrl}/?includeAccounts=true`);
  }

  getCustomerById(id: number): Observable<Customer> {
    return this.http.get<Customer>(`${this.apiUrl}/${id}`);
  }

  getCustomerWithAccounts(id: number): Observable<CustomerWithAccountsDTO> {
    return this.http.get<CustomerWithAccountsDTO>(`${this.apiUrl}/${id}?includeAccounts=true`);
  }

  createCustomer(customer: CustomerRequestDTO): Observable<Customer> {
    return this.http.post<Customer>(this.apiUrl, customer);
  }

  updateCustomer(id: number, customer: CustomerRequestDTO): Observable<Customer> {
    return this.http.put<Customer>(`${this.apiUrl}/${id}`, customer);
  }

  deleteCustomer(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
