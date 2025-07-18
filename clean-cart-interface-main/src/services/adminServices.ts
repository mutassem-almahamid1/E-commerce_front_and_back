import { apiClient } from '@/lib/api';
import { Product, Category, Brand, User, ProductRequest, CategoryRequest, BrandRequest } from '@/types';

// Admin Product Management
export const adminProductService = {
  async createProduct(productData: ProductRequest): Promise<Product> {
    return await apiClient.createProduct(productData);
  },

  async updateProduct(id: string, productData: ProductRequest): Promise<Product> {
    return await apiClient.updateProduct(id, productData);
  },

  async deleteProduct(id: string): Promise<void> {
    return await apiClient.deleteProduct(id);
  },

  async getAllProducts(page = 0, size = 100) {
    return await apiClient.getProducts(page, size);
  },
};

// Admin Category Management
export const adminCategoryService = {
  async createCategory(categoryData: CategoryRequest): Promise<Category> {
    return await apiClient.createCategory(categoryData);
  },

  async updateCategory(id: string, categoryData: CategoryRequest): Promise<Category> {
    return await apiClient.updateCategory(id, categoryData);
  },

  async deleteCategory(id: string): Promise<void> {
    return await apiClient.deleteCategory(id);
  },

  async getAllCategories(): Promise<Category[]> {
    return await apiClient.getActiveCategories();
  },
};

// Admin Brand Management
export const adminBrandService = {
  async createBrand(brandData: BrandRequest): Promise<Brand> {
    return await apiClient.createBrand(brandData);
  },

  async updateBrand(id: string, brandData: BrandRequest): Promise<Brand> {
    return await apiClient.updateBrand(id, brandData);
  },

  async deleteBrand(id: string): Promise<void> {
    return await apiClient.deleteBrand(id);
  },

  async getAllBrands(): Promise<Brand[]> {
    return await apiClient.getActiveBrands();
  },
};

// Admin User Management
export const adminUserService = {
  async getAllUsers(): Promise<User[]> {
    return await apiClient.getAllUsers();
  },

  async updateUserStatus(id: string, status: 'ACTIVE' | 'BLOCKED'): Promise<User> {
    return await apiClient.updateUserStatus(id, status);
  },

  async getUserAnalytics() {
    // This would be implemented when backend provides analytics endpoints
    return {
      totalUsers: 0,
      activeUsers: 0,
      blockedUsers: 0,
      newUsersThisMonth: 0,
    };
  },
};

// Analytics Service (for future expansion)
export const analyticsService = {
  async getDashboardStats() {
    // This would call backend analytics endpoints
    return {
      totalProducts: 0,
      totalOrders: 0,
      totalRevenue: 0,
      averageRating: 0,
      topSellingProducts: [],
      recentOrders: [],
    };
  },
};
