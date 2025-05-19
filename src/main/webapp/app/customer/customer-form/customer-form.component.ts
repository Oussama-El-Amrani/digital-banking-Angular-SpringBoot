import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CustomerService } from '../../services/customer.service';
import { Customer } from '../../models/customer.model';

@Component({
  selector: 'app-customer-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './customer-form.component.html'
})
export class CustomerFormComponent implements OnInit {
  customerForm!: FormGroup;
  isEditMode = false;
  customerId?: number;
  loading = false;
  submitted = false;
  errorMessage = '';

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private customerService: CustomerService
  ) { }

  ngOnInit(): void {
    this.initForm();

    // Check if we're in edit mode
    this.customerId = Number(this.route.snapshot.paramMap.get('id'));
    if (this.customerId) {
      this.isEditMode = true;
      this.loadCustomer(this.customerId);
    }
  }

  initForm(): void {
    this.customerForm = this.formBuilder.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]]
    });
  }

  loadCustomer(id: number): void {
    this.loading = true;
    this.customerService.getCustomerById(id).subscribe({
      next: (customer) => {
        this.customerForm.patchValue({
          name: customer.name,
          email: customer.email
        });
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = 'Error loading customer: ' + (err.message || 'Unknown error');
        this.loading = false;
      }
    });
  }

  onSubmit(): void {
    this.submitted = true;

    if (this.customerForm.invalid) {
      return;
    }

    this.loading = true;
    const customerData = {
      name: this.customerForm.value.name,
      email: this.customerForm.value.email
    };

    if (this.isEditMode && this.customerId) {
      this.customerService.updateCustomer(this.customerId, customerData).subscribe({
        next: () => {
          this.router.navigate(['/customers']);
        },
        error: (err) => {
          console.error('Error updating customer:', err);
          this.errorMessage = 'Error updating customer: ' + (err.error?.message || 'Please try again later');
          this.loading = false;
        }
      });
    } else {
      this.customerService.createCustomer(customerData).subscribe({
        next: () => {
          this.router.navigate(['/customers']);
        },
        error: (err) => {
          console.error('Error creating customer:', err);
          this.errorMessage = 'Error creating customer: ' + (err.error?.message || 'Please try again later');
          this.loading = false;
        }
      });
    }
  }
}
