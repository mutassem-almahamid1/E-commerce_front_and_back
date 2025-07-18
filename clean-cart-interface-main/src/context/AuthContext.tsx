import React, { createContext, useContext, useEffect, ReactNode } from 'react';
import { User } from '@/types';
import { useAppSelector, useAppDispatch } from '@/store/hooks';
import { checkAuthStatus, loginUser, signupUser, logoutUser } from '@/store/slices/authSlice';
import { UserLoginRequest, UserSignUpRequest } from '@/types';

interface AuthContextType {
  user: User | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  login: (credentials: UserLoginRequest) => Promise<void>;
  signup: (userData: UserSignUpRequest) => Promise<void>;
  logout: () => Promise<void>;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

interface AuthProviderProps {
  children: ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const dispatch = useAppDispatch();
  const { user, isAuthenticated, status } = useAppSelector((state) => state.auth);

  useEffect(() => {
    // Only check authentication status once on app initialization
    if (status === 'idle') {
      dispatch(checkAuthStatus());
    }
  }, [dispatch]); // Remove status dependency to prevent infinite loop

  const login = async (credentials: UserLoginRequest) => {
    await dispatch(loginUser(credentials)).unwrap();
  };

  const signup = async (userData: UserSignUpRequest) => {
    await dispatch(signupUser(userData)).unwrap();
  };

  const logout = async () => {
    await dispatch(logoutUser()).unwrap();
  };

  const value: AuthContextType = {
    user,
    isAuthenticated,
    isLoading: status === 'loading',
    login,
    signup,
    logout,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};