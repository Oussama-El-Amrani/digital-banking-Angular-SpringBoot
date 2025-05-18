import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CustomerService } from '../../services/customer.service';
import { BankAccountService } from '../../services/bank-account.service';
import { Customer } from '../../models/customer.model';
import { BankAccountDTO } from '../../models/bank-account.model';

@Component({
  selector: 'app-customer-details',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './customer-details.component.html'
})
export class CustomerDetailsComponent implements OnInit {
  customer: Customer | null = null;
  accounts: BankAccountDTO[] = [];
  loading = true;
  loadingAccounts = true;
  errorMessage = '';

  constructor(
    private route: ActivatedRoute,
    private customerService: CustomerService,
    private bankAccountService: BankAccountService
  ) { }

  ngOnInit(): void {
    const customerId = Number(this.route.snapshot.paramMap.get('id'));
    if (customerId) {
      this.loadCustomer(customerId);
      this.loadCustomerAccounts(customerId);
    } else {
      this.errorMessage = 'Customer ID is required';
      this.loading = false;
    }
  }

  loadCustomer(id: number): void {
    this.loading = true;
    this.customerService.getCustomerById(id).subscribe({
      next: (data) => {
        this.customer = data;
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = 'Error loading customer: ' + (err.message || 'Unknown error');
        this.loading = false;
      }
    });
  }

  loadCustomerAccounts(customerId: number): void {
    this.loadingAccounts = true;
    this.bankAccountService.getAccountsByCustomerId(customerId).subscribe({
      next: (data) => {
        this.accounts = data;
        this.loadingAccounts = false;
      },
      error: (err) => {
        this.errorMessage = 'Error loading accounts: ' + (err.message || 'Unknown error');
        this.loadingAccounts = false;
      }
    });
  }
}
