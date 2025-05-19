import {Routes} from '@angular/router';
import {HomeComponent} from './home/home.component';
import {ErrorComponent} from './error/error.component';
import {CustomerListComponent} from './customer/customer-list/customer-list.component';
import {CustomerFormComponent} from './customer/customer-form/customer-form.component';
import {CustomerDetailsComponent} from './customer/customer-details/customer-details.component';
import {AccountFormComponent} from './account/account-form/account-form.component';
import {AccountOperationsComponent} from './account/account-operations/account-operations.component';
import {BankingDashboardComponent} from './banking-dashboard/banking-dashboard.component';
import {ApiDocsComponent} from './api-docs/api-docs.component';
import {LoginComponent} from './auth/login/login.component';
import {RegisterComponent} from './auth/register/register.component';
import {CustomerAccountsComponent} from './customer/customer-accounts/customer-accounts.component';
import {AuthGuard} from './guards/auth.guard';
import {AdminGuard} from './guards/admin.guard';
import {CustomerGuard} from './guards/customer.guard';

export const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    title: $localize`:@@home.index.headline:Welcome to your new app!`
  },
  {
    path: 'login',
    component: LoginComponent,
    title: 'Login'
  },
  {
    path: 'register',
    component: RegisterComponent,
    title: 'Register'
  },
  {
    path: 'banking',
    component: BankingDashboardComponent,
    title: 'Digital Banking Dashboard',
    canActivate: [AuthGuard]
  },
  // Admin routes
  {
    path: 'customers',
    component: CustomerListComponent,
    title: 'Customers',
    canActivate: [AdminGuard]
  },
  {
    path: 'customers/new',
    component: CustomerFormComponent,
    title: 'Add New Customer',
    canActivate: [AdminGuard]
  },
  {
    path: 'customers/:id',
    component: CustomerDetailsComponent,
    title: 'Customer Details',
    canActivate: [AdminGuard]
  },
  {
    path: 'customers/:id/edit',
    component: CustomerFormComponent,
    title: 'Edit Customer',
    canActivate: [AdminGuard]
  },
  {
    path: 'accounts/new',
    component: AccountFormComponent,
    title: 'Create New Account',
    canActivate: [AdminGuard]
  },
  {
    path: 'accounts/:id/operations',
    component: AccountOperationsComponent,
    title: 'Account Operations',
    canActivate: [AdminGuard]
  },
  // Customer routes
  {
    path: 'customer/accounts',
    component: CustomerAccountsComponent,
    title: 'My Accounts',
    canActivate: [CustomerGuard]
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
