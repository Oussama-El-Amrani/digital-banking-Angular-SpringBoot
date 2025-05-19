import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { CustomerAccountService } from '../../services/customer-account.service';
import { BankAccountDTO } from '../../models/bank-account.model';

@Component({
  selector: 'app-customer-accounts',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './customer-accounts.component.html'
})
export class CustomerAccountsComponent implements OnInit {
  accounts: BankAccountDTO[] = [];
  loading = true;
  error = '';

  constructor(private customerAccountService: CustomerAccountService) { }

  ngOnInit(): void {
    this.loadAccounts();
  }

  loadAccounts(): void {
    this.loading = true;
    this.customerAccountService.getMyAccounts().subscribe({
      next: (data) => {
        this.accounts = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load accounts: ' + (err.error?.message || 'Unknown error');
        this.loading = false;
      }
    });
  }
}
