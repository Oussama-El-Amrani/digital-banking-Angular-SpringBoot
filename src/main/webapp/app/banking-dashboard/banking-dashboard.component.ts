import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators, FormsModule } from '@angular/forms';
import { CustomerService } from '../services/customer.service';
import { BankAccountService } from '../services/bank-account.service';
import { Customer } from '../models/customer.model';
import { BankAccountDTO } from '../models/bank-account.model';

interface CustomerWithAccounts extends Customer {
  bankAccountIds: string[];
}

@Component({
  selector: 'app-banking-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, ReactiveFormsModule, FormsModule],
  templateUrl: './banking-dashboard.component.html'
})
export class BankingDashboardComponent implements OnInit {
  // Form
  transferForm!: FormGroup;

  // Customers and accounts
  customers: Customer[] = [];
  customerAccounts: BankAccountDTO[] = [];
  sourceCustomerAccounts: BankAccountDTO[] = [];
  destinationCustomerAccounts: BankAccountDTO[] = [];

  // Selected values
  selectedCustomerId: number | null = null;
  selectedAccountId: string | null = null;

  // State management
  loadingAccounts = false;
  isTransferring = false;
  errorMessage = '';

  constructor(
    private router: Router,
    private formBuilder: FormBuilder,
    private customerService: CustomerService,
    private bankAccountService: BankAccountService
  ) {
    this.transferForm = this.formBuilder.group({
      sourceCustomerId: ['', Validators.required],
      sourceAccountId: ['', Validators.required],
      destinationCustomerId: ['', Validators.required],
      destinationAccountId: ['', Validators.required],
      amount: ['', [Validators.required, Validators.min(0.01)]],
      description: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.loadCustomers();
  }

  loadCustomers(): void {
    this.customerService.getAllCustomers().subscribe({
      next: (data) => {
        this.customers = data;
      },
      error: (err) => {
        this.errorMessage = 'Error loading customers: ' + (err.message || 'Unknown error');
      }
    });
  }

  loadCustomerAccounts(event: Event): void {
    const selectElement = event.target as HTMLSelectElement;
    const customerId = Number(selectElement.value);

    if (customerId) {
      this.selectedCustomerId = customerId;
      this.selectedAccountId = null;
      this.loadingAccounts = true;

      this.customerService.getCustomerWithAccounts(customerId).subscribe({
        next: (customer: CustomerWithAccounts) => {
          this.loadAccountDetailsForCustomer(customer);
        },
        error: (err) => {
          this.loadingAccounts = false;
          this.errorMessage = 'Error loading customer details: ' + (err.message || 'Unknown error');
        }
      });
    } else {
      this.selectedCustomerId = null;
      this.customerAccounts = [];
    }
  }

  loadAccountDetailsForCustomer(customer: CustomerWithAccounts): void {
    if (customer && customer.bankAccountIds && customer.bankAccountIds.length > 0) {
      // We need to fetch details for each account ID
      const accountIds = customer.bankAccountIds;
      this.customerAccounts = [];

      // Create an array of observables for each account
      const accountObservables = accountIds.map((id: string) =>
        this.bankAccountService.getAccountById(id)
      );

      // Subscribe to all observables
      let completedRequests = 0;
      accountObservables.forEach(observable => {
        observable.subscribe({
          next: (account: BankAccountDTO) => {
            this.customerAccounts.push(account);
            completedRequests++;

            if (completedRequests === accountIds.length) {
              this.loadingAccounts = false;
            }
          },
          error: (err) => {
            completedRequests++;
            console.error('Error loading account details:', err);

            if (completedRequests === accountIds.length) {
              this.loadingAccounts = false;
            }
          }
        });
      });
    } else {
      this.customerAccounts = [];
      this.loadingAccounts = false;
    }
  }

  loadSourceCustomerAccounts(): void {
    const customerId = this.transferForm.get('sourceCustomerId')?.value;
    if (customerId) {
      this.transferForm.get('sourceAccountId')?.setValue('');

      this.customerService.getCustomerWithAccounts(customerId).subscribe({
        next: (customer: CustomerWithAccounts) => {
          if (customer && customer.bankAccountIds && customer.bankAccountIds.length > 0) {
            // Clear previous accounts
            this.sourceCustomerAccounts = [];

            // Fetch details for each account
            const accountIds = customer.bankAccountIds;
            const accountObservables = accountIds.map((id: string) =>
              this.bankAccountService.getAccountById(id)
            );

            let completedRequests = 0;
            accountObservables.forEach(observable => {
              observable.subscribe({
                next: (account: BankAccountDTO) => {
                  this.sourceCustomerAccounts.push(account);
                  completedRequests++;
                },
                error: (err) => {
                  completedRequests++;
                  console.error('Error loading source account details:', err);
                }
              });
            });
          } else {
            this.sourceCustomerAccounts = [];
          }
        },
        error: (err) => {
          this.errorMessage = 'Error loading source customer accounts: ' + (err.message || 'Unknown error');
        }
      });
    } else {
      this.sourceCustomerAccounts = [];
    }
  }

  loadDestinationCustomerAccounts(): void {
    const customerId = this.transferForm.get('destinationCustomerId')?.value;
    if (customerId) {
      this.transferForm.get('destinationAccountId')?.setValue('');

      this.customerService.getCustomerWithAccounts(customerId).subscribe({
        next: (customer: CustomerWithAccounts) => {
          if (customer && customer.bankAccountIds && customer.bankAccountIds.length > 0) {
            // Clear previous accounts
            this.destinationCustomerAccounts = [];

            // Fetch details for each account
            const accountIds = customer.bankAccountIds;
            const accountObservables = accountIds.map((id: string) =>
              this.bankAccountService.getAccountById(id)
            );

            let completedRequests = 0;
            accountObservables.forEach(observable => {
              observable.subscribe({
                next: (account: BankAccountDTO) => {
                  this.destinationCustomerAccounts.push(account);
                  completedRequests++;
                },
                error: (err) => {
                  completedRequests++;
                  console.error('Error loading destination account details:', err);
                }
              });
            });
          } else {
            this.destinationCustomerAccounts = [];
          }
        },
        error: (err) => {
          this.errorMessage = 'Error loading destination customer accounts: ' + (err.message || 'Unknown error');
        }
      });
    } else {
      this.destinationCustomerAccounts = [];
    }
  }

  viewAccount(): void {
    if (this.selectedAccountId) {
      this.router.navigate(['/accounts', this.selectedAccountId, 'operations']);
    }
  }

  onTransferSubmit(): void {
    if (this.transferForm.invalid) {
      return;
    }

    this.isTransferring = true;
    this.errorMessage = '';

    const sourceAccountId = this.transferForm.get('sourceAccountId')?.value;
    const destinationAccountId = this.transferForm.get('destinationAccountId')?.value;
    const amount = parseFloat(this.transferForm.get('amount')?.value);
    const description = this.transferForm.get('description')?.value;

    this.bankAccountService.transfer(sourceAccountId, destinationAccountId, amount, description).subscribe({
      next: () => {
        this.isTransferring = false;
        // Refresh account data after successful transfer
        this.loadSourceCustomerAccounts();
        this.loadDestinationCustomerAccounts();

        // Reset amount and description fields
        this.transferForm.patchValue({
          amount: '',
          description: ''
        });

        // Show success message
        alert('Transfer completed successfully');
      },
      error: (err) => {
        this.isTransferring = false;
        this.errorMessage = 'Error performing transfer: ' + (err.error || err.message || 'Unknown error');
      }
    });
  }
}
