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
  balance: number;
  createdAt: Date;
  status: string;
  customerDTO: {
    id: number;
    name: string;
    email: string;
  };
  type: AccountType;
  interestRate?: number;
  overDraft?: number;
}
