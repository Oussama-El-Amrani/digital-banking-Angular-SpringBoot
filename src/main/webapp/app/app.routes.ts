import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { ErrorComponent } from './error/error.component';
import { CustomerListComponent } from './customer/customer-list/customer-list.component';
import { CustomerFormComponent } from './customer/customer-form/customer-form.component';
import { CustomerDetailsComponent } from './customer/customer-details/customer-details.component';
import { AccountFormComponent } from './account/account-form/account-form.component';
import { AccountOperationsComponent } from './account/account-operations/account-operations.component';
import { BankingDashboardComponent } from './banking-dashboard/banking-dashboard.component';
import { ApiDocsComponent } from './api-docs/api-docs.component';

export const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    title: $localize`:@@home.index.headline:Welcome to your new app!`
  },
  {
    path: 'banking',
    component: BankingDashboardComponent,
    title: 'Digital Banking Dashboard'
  },
  {
    path: 'customers',
    component: CustomerListComponent,
    title: 'Customers'
  },
  {
    path: 'customers/new',
    component: CustomerFormComponent,
    title: 'Add New Customer'
  },
  {
    path: 'customers/:id',
    component: CustomerDetailsComponent,
    title: 'Customer Details'
  },
  {
    path: 'customers/:id/edit',
    component: CustomerFormComponent,
    title: 'Edit Customer'
  },
  {
    path: 'accounts/new',
    component: AccountFormComponent,
    title: 'Create New Account'
  },
  {
    path: 'accounts/:id/operations',
    component: AccountOperationsComponent,
    title: 'Account Operations'
  },
  {
    path: 'api-docs',
    component: ApiDocsComponent,
    title: 'API Documentation'
  },
  {
    path: 'error',
    component: ErrorComponent,
    title: $localize`:@@error.page.headline:Error`
  },
  {
    path: '**',
    component: ErrorComponent,
    title: $localize`:@@notFound.headline:Page not found`
  }
];
