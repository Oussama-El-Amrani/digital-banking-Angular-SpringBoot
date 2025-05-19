export enum AccountType {
  CURRENT = 'CURRENT',
  SAVING = 'SAVING'
}

export interface BankAccount {
  id: string;
  balance: number;
  createdAt: Date;
  status: string;
  customerId: number;
  type: AccountType;
  interestRate?: number; // For SAVING accounts
  overDraft?: number; // For CURRENT accounts
}

export interface BankAccountDTO {
  id: string;
  accountNumber: string;
  accountType: string;
  currency: string;
  balance: number;
  customerId: number;
  customerName: string;
}

export interface CurrentAccountDTO extends BankAccountDTO {
  overdraftLimit: number;
}

export interface SavingAccountDTO extends BankAccountDTO {
  interestRate: number;
  dailyWithdrawn: number;
}

export interface TransactionRequest {
  accountId: string;
  amount: number;
  description?: string;
}

export interface TransferRequest {
  sourceAccountId: string;
  destinationAccountId: string;
  amount: number;
  description?: string;
}
