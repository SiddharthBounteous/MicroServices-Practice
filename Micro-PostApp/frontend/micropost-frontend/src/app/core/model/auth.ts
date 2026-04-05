export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
  role: string;
}

export interface LoginResponse {
  userId: string;
  username: string;
  token: string;
}

export interface RegisterResponse {
  message: string;
}