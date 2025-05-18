import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { BankAccountService } from '../../services/bank-account.service';
import { AccountOperationService } from '../../services/account-operation.service';
import { BankAccountDTO } from '../../models/bank-account.model';
import { AccountHistoryDTO, OperationType } from '../../models/account-operation.model';

@Component({
  selector: 'app-account-operations',
  standalone: true,
  imports: [CommonModule, RouterModule, ReactiveFormsModule],
  templateUrl: './account-operations.component.html'
})
export class AccountOperationsComponent implements OnInit {
  accountId: string = '';
  account: BankAccountDTO | null = null;
  accountHistory: AccountHistoryDTO | null = null;
  loading = true;
  loadingHistory = true;
  operationLoading = false;
  errorMessage = '';
  currentPage = 0;
  pageSize = 5;
  debitForm!: FormGroup;
  creditForm!: FormGroup;

  constructor(
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private bankAccountService: BankAccountService,
    private accountOperationService: AccountOperationService
  ) { }

  ngOnInit(): void {
    this.initForms();

    this.accountId = this.route.snapshot.paramMap.get('id') || '';
    if (this.accountId) {
      this.loadAccount();
      this.loadAccountHistory();
    } else {
      this.errorMessage = 'Account ID is required';
      this.loading = false;
    }
  }

  initForms(): void {
    this.debitForm = this.formBuilder.group({
      amount: [null, [Validators.required, Validators.min(0.01)]],
      description: ['', Validators.required]
    });

    this.creditForm = this.formBuilder.group({
      amount: [null, [Validators.required, Validators.min(0.01)]],
      description: ['', Validators.required]
    });
  }

  loadAccount(): void {
    this.loading = true;
    this.bankAccountService.getAccountById(this.accountId).subscribe({
      next: (data) => {
        this.account = data;
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = 'Error loading account: ' + (err.message || 'Unknown error');
        this.loading = false;
      }
    });
  }

  loadAccountHistory(): void {
    this.loadingHistory = true;
    this.accountOperationService.getOperationsByAccountId(this.accountId, this.currentPage, this.pageSize).subscribe({
      next: (data) => {
        this.accountHistory = data;
        this.loadingHistory = false;
      },
      error: (err) => {
        this.errorMessage = 'Error loading operations: ' + (err.message || 'Unknown error');
        this.loadingHistory = false;
      }
    });
  }

  onDebit(): void {
    if (this.debitForm.invalid) return;

    this.operationLoading = true;
    const amount = this.debitForm.value.amount;
    const description = this.debitForm.value.description;

    this.bankAccountService.debit(this.accountId, amount, description).subscribe({
      next: () => {
        this.operationLoading = false;
        this.debitForm.reset();
        // Reload account and history
        this.loadAccount();
        this.loadAccountHistory();
      },
      error: (err) => {
        this.errorMessage = 'Error performing debit operation: ' + (err.message || 'Unknown error');
        this.operationLoading = false;
      }
    });
  }

  onCredit(): void {
    if (this.creditForm.invalid) return;

    this.operationLoading = true;
    const amount = this.creditForm.value.amount;
    const description = this.creditForm.value.description;

    this.bankAccountService.credit(this.accountId, amount, description).subscribe({
      next: () => {
        this.operationLoading = false;
        this.creditForm.reset();
        // Reload account and history
        this.loadAccount();
        this.loadAccountHistory();
      },
      error: (err) => {
        this.errorMessage = 'Error performing credit operation: ' + (err.message || 'Unknown error');
        this.operationLoading = false;
      }
    });
  }

  goToPage(page: number): void {
    if (this.accountHistory && page >= 0 && page < this.accountHistory.totalPages) {
      this.currentPage = page;
      this.loadAccountHistory();
    }
  }

  getPageNumbers(): number[] {
    if (!this.accountHistory) return [];
    return Array(this.accountHistory.totalPages).fill(0).map((_, i) => i);
  }
}
