import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { BankAccountService } from '../../services/bank-account.service';
import { CustomerService } from '../../services/customer.service';
import { AccountType } from '../../models/bank-account.model';
import { Customer } from '../../models/customer.model';

@Component({
  selector: 'app-account-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './account-form.component.html'
})
export class AccountFormComponent implements OnInit {
  accountForm!: FormGroup;
  customers: Customer[] = [];
  loading = false;
  submitted = false;
  errorMessage = '';
  preSelectedCustomerId: number | null = null;
  AccountType = AccountType; // Expose enum to template

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private bankAccountService: BankAccountService,
    private customerService: CustomerService
  ) { }

  ngOnInit(): void {
    this.initForm();
    this.loadCustomers();

    // Check if a customer ID was provided in query params
    const customerId = this.route.snapshot.queryParamMap.get('customerId');
    if (customerId) {
      this.preSelectedCustomerId = Number(customerId);
      this.accountForm.patchValue({ customerId: this.preSelectedCustomerId });
    }

    // Update validation based on account type
    this.accountForm.get('type')?.valueChanges.subscribe(type => {
      if (type === AccountType.CURRENT) {
        this.accountForm.get('overDraft')?.setValidators([Validators.required, Validators.min(0)]);
        this.accountForm.get('interestRate')?.clearValidators();
      } else if (type === AccountType.SAVING) {
        this.accountForm.get('interestRate')?.setValidators([Validators.required, Validators.min(0), Validators.max(100)]);
        this.accountForm.get('overDraft')?.clearValidators();
      }
      this.accountForm.get('overDraft')?.updateValueAndValidity();
      this.accountForm.get('interestRate')?.updateValueAndValidity();
    });
  }

  initForm(): void {
    this.accountForm = this.formBuilder.group({
      customerId: ['', Validators.required],
      balance: [0, [Validators.required, Validators.min(0)]],
      type: [AccountType.CURRENT, Validators.required],
      overDraft: [0, [Validators.required, Validators.min(0)]],
      interestRate: [0]
    });
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

  onSubmit(): void {
    this.submitted = true;

    if (this.accountForm.invalid) {
      return;
    }

    this.loading = true;
    const customerId = Number(this.accountForm.value.customerId);
    const initialBalance = Number(this.accountForm.value.balance);
    const accountType = this.accountForm.value.type;

    if (accountType === AccountType.CURRENT) {
      const overdraftLimit = Number(this.accountForm.value.overDraft);
      this.bankAccountService.createCurrentAccount(customerId, initialBalance, overdraftLimit).subscribe({
        next: (response) => {
          this.loading = false;
          this.router.navigate(['/customers', customerId]);
        },
        error: (err) => {
          this.errorMessage = 'Error creating account: ' + (err.message || 'Unknown error');
          this.loading = false;
        }
      });
    } else {
      const interestRate = Number(this.accountForm.value.interestRate);
      this.bankAccountService.createSavingAccount(customerId, initialBalance, interestRate).subscribe({
        next: (response) => {
          this.loading = false;
          this.router.navigate(['/customers', customerId]);
        },
        error: (err) => {
          this.errorMessage = 'Error creating account: ' + (err.message || 'Unknown error');
          this.loading = false;
        }
      });
    }
  }
}
