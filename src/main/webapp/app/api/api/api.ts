export * from './account-operation-controller.service';
import { AccountOperationControllerService } from './account-operation-controller.service';
export * from './bank-account-controller.service';
import { BankAccountControllerService } from './bank-account-controller.service';
export * from './customer-controller.service';
import { CustomerControllerService } from './customer-controller.service';
export const APIS = [AccountOperationControllerService, BankAccountControllerService, CustomerControllerService];
