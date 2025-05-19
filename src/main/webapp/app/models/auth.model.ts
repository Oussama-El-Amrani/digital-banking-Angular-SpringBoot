export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  password: string;
  name: string;
  email: string;
  phoneNumber?: string;
  address?: string;
  roles?: string[];
}

export interface JwtResponse {
  token: string;
  type: string;
  id: number;
  username: string;
  roles: string[];
  customerId?: number;
}

export interface User {
  id: number;
  username: string;
  roles: string[];
  customerId?: number;
  token?: string;
}
