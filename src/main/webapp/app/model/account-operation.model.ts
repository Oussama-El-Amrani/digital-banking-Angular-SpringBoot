export enum OperationType {
  DEBIT = 'DEBIT',
  CREDIT = 'CREDIT'
}

export interface AccountOperation {
  id: number;
  operationDate: Date;
  amount: number;
  description: string;
  type: OperationType;
  bankAccountId: string;
}

export interface AccountOperationDTO {
  id: number;
  operationDate: Date;
  amount: number;
  description: string;
  type: OperationType;
  bankAccountDTO: {
    id: string;
    balance: number;
  };
}

export interface AccountHistoryDTO {
  accountId: string;
  balance: number;
  currentPage: number;
  totalPages: number;
  pageSize: number;
  accountOperationDTOS: AccountOperationDTO[];
}
