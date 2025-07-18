import axios, { AxiosInstance, AxiosError } from 'axios';
import { 
  User, 
  UserLoginRequest, 
  UserSignUpRequest, 
  JwtResponse, 
  Product, 
  Category, 
  Brand, 
  Review, 
  ReviewRequest,
  Page,
  ProductRequest,
  CategoryRequest,
  BrandRequest,
  Order,
  OrderRequest,
  Cart,
  CartItem
} from '@/types';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1';

class ApiClient {
  private client: AxiosInstance;
  private isRefreshing = false;
  private failedQueue: Array<{
    resolve: (value?: any) => void;
    reject: (error?: any) => void;
  }> = [];

  constructor() {
    this.client = axios.create({
      baseURL: API_BASE_URL,
      headers: {
        'Content-Type': 'application/json',
      },
      withCredentials: true, // Enable sending cookies with requests
    });

    // Request interceptor - cookies are automatically sent with withCredentials: true
    this.client.interceptors.request.use((config) => {
      return config;
    });

    // Response interceptor to handle errors and token refresh
    this.client.interceptors.response.use(
      (response) => response,
      async (error: AxiosError) => {
        const originalRequest = error.config as any;

        if (error.response?.status === 401 && !originalRequest._retry) {
          if (this.isRefreshing) {
            // If refresh is in progress, queue the request
            return new Promise((resolve, reject) => {
              this.failedQueue.push({ resolve, reject });
            }).then(() => {
              return this.client(originalRequest);
            }).catch(err => {
              return Promise.reject(err);
            });
          }

          originalRequest._retry = true;
          this.isRefreshing = true;

          try {
            // Try to refresh the token
            await this.client.post('/auth/refresh');
            this.processQueue(null);
            return this.client(originalRequest);
          } catch (refreshError) {
            this.processQueue(refreshError);
            // Don't redirect automatically - let the app handle this through Redux
            console.log('Token refresh failed, user needs to login again');
            return Promise.reject(refreshError);
          } finally {
            this.isRefreshing = false;
          }
        }

        // Handle other errors
        if (error.response?.status === 403) {
          console.error('Access forbidden');
        } else if (error.response?.status >= 500) {
          console.error('Server error');
        }

        return Promise.reject(error);
      }
    );
  }

  private processQueue(error: any) {
    this.failedQueue.forEach(({ resolve, reject }) => {
      if (error) {
        reject(error);
      } else {
        resolve();
      }
    });

    this.failedQueue = [];
  }

  // Auth endpoints
  async login(credentials: UserLoginRequest): Promise<User> {
    try {
      const response = await this.client.post('/auth/login', credentials);
      // الباك إند يرجع JwtResponse وليس User مباشرة
      const jwtResponse = response.data;

      // نحتاج لاستخراج بيانات المستخدم من JWT response أو استدعاء /auth/me
      const userResponse = await this.getCurrentUser();
      return userResponse;
    } catch (error: any) {
      console.error('Login error:', error);
      throw new Error(error.response?.data?.message || 'Login failed');
    }
  }

  async signup(userData: UserSignUpRequest): Promise<User> {
    try {
      const response = await this.client.post('/auth/signup', userData);
      return response.data;
    } catch (error: any) {
      console.error('Signup error:', error);
      throw new Error(error.response?.data?.message || 'Signup failed');
    }
  }

  async logout(): Promise<void> {
    try {
      await this.client.post('/auth/logout');
    } catch (error: any) {
      console.error('Logout error:', error);
      // لا نرمي خطأ في تسجيل الخروج لأنه قد يكون المستخدم مسجل خروج بالفعل
    }
  }

  async getCurrentUser(): Promise<User> {
    try {
      const response = await this.client.get('/auth/me');
      return response.data;
    } catch (error: any) {
      console.error('Get current user error:', error);
      throw new Error('Not authenticated');
    }
  }

  async refreshToken(): Promise<void> {
    try {
      await this.client.post('/auth/refresh');
    } catch (error: any) {
      console.error('Refresh token error:', error);
      throw new Error('Session expired');
    }
  }

  // Product endpoints
  async getProducts(page = 0, size = 10): Promise<Page<Product>> {
    const response = await this.client.get(`/products?page=${page}&size=${size}`);
    return response.data;
  }

  async getProductById(id: string): Promise<Product> {
    const response = await this.client.get(`/products/${id}`);
    return response.data;
  }

  async getNewestProducts(): Promise<Product[]> {
    const response = await this.client.get('/products/newest');
    return response.data;
  }

  async getTopRatedProducts(): Promise<Product[]> {
    const response = await this.client.get('/products/top-rated');
    return response.data;
  }

  // جلب المنتجات حسب التصنيف - استخدام اسم التصنيف بدلاً من المعرّف
  async getProductsByCategory(categoryName: string, page = 0, size = 12): Promise<Page<Product>> {
    const response = await this.client.get(`/products/category/${categoryName}?page=${page}&size=${size}`);
    return response.data;
  }

  // جلب المنتجات حسب البراند - تأكيد استخدام اسم البراند
  async getProductsByBrand(brandName: string, page = 0, size = 12): Promise<Page<Product>> {
    const response = await this.client.get(`/products/brand/${brandName}?page=${page}&size=${size}`);
    return response.data;
  }

  // Search products by name
  async searchProducts(name: string, page = 0, size = 12): Promise<Page<Product>> {
    const response = await this.client.get(`/products/search?name=${name}&page=${page}&size=${size}`);
    return response.data;
  }

  // Category endpoints
  async getActiveCategories(): Promise<Category[]> {
    const response = await this.client.get('/categories/active');
    // الباك إند يرجع قائمة مباشرة وليس هيكل بيانات يحتوي على content
    return Array.isArray(response.data) ? response.data : [];
  }

  // Brand endpoints
  async getActiveBrands(): Promise<Brand[]> {
    // استخدام الإندبوينت /brands/simple بدلاً من /brands/active لأنه يعيد قائمة بسيطة
    // بينما /brands/active يتطلب معلمات الصفحة ويعيد Page<BrandResponse>
    const response = await this.client.get('/brands/simple');
    return Array.isArray(response.data) ? response.data : [];
  }

  // Review endpoints
  async getReviewsByProductId(productId: string): Promise<Review[]> {
    const response = await this.client.get(`/reviews/product/${productId}`);
    return response.data;
  }

  async submitReview(review: ReviewRequest & { userId: string }): Promise<Review> {
    const response = await this.client.post(`/reviews?userId=${review.userId}`, review);
    return response.data;
  }

  // Admin endpoints
  async createProduct(productData: ProductRequest): Promise<Product> {
    const response = await this.client.post('/products', productData);
    return response.data;
  }

  async updateProduct(id: string, productData: ProductRequest): Promise<Product> {
    const response = await this.client.put(`/products/${id}`, productData);
    return response.data;
  }

  async deleteProduct(id: string): Promise<void> {
    await this.client.delete(`/products/${id}/hard`);
  }

  async createCategory(categoryData: CategoryRequest): Promise<Category> {
    const response = await this.client.post('/categories', categoryData);
    return response.data;
  }

  async updateCategory(id: string, categoryData: CategoryRequest): Promise<Category> {
    const response = await this.client.put(`/categories/${id}`, categoryData);
    return response.data;
  }

  async deleteCategory(id: string): Promise<void> {
    await this.client.delete(`/categories/${id}`);
  }

  async createBrand(brandData: BrandRequest): Promise<Brand> {
    const response = await this.client.post('/brands', brandData);
    return response.data;
  }

  async updateBrand(id: string, brandData: BrandRequest): Promise<Brand> {
    const response = await this.client.put(`/brands/${id}`, brandData);
    return response.data;
  }

  async deleteBrand(id: string): Promise<void> {
    await this.client.delete(`/brands/${id}`);
  }

  async getAllUsers(): Promise<User[]> {
    const response = await this.client.get('/users');
    return response.data;
  }

  async updateUserStatus(id: string, status: 'ACTIVE' | 'BLOCKED'): Promise<User> {
    const response = await this.client.put(`/users/${id}/status`, { status });
    return response.data;
  }

  // Cart endpoints
  async getCart(userId: string): Promise<Cart> {
    const response = await this.client.get(`/cart/${userId}`);
    return response.data;
  }

  async addToCart(userId: string, cartItem: CartItem): Promise<Cart> {
    const response = await this.client.post(`/cart/${userId}/add`, cartItem);
    return response.data;
  }

  async removeFromCart(userId: string, productId: string): Promise<Cart> {
    const response = await this.client.delete(`/cart/${userId}/remove/${productId}`);
    return response.data;
  }

  async clearCart(userId: string): Promise<void> {
    await this.client.delete(`/cart/${userId}/clear`);
  }

  // Order endpoints
  async createOrder(userId: string, orderData: OrderRequest): Promise<Order> {
    const response = await this.client.post(`/orders/${userId}`, orderData);
    return response.data;
  }

  async getOrders(userId: string): Promise<Order[]> {
    const response = await this.client.get(`/orders/user/${userId}`);
    return response.data;
  }

  async getOrderById(orderId: string): Promise<Order> {
    const response = await this.client.get(`/orders/${orderId}`);
    return response.data;
  }

  // Generic HTTP methods for flexibility
  async get(url: string) {
    return this.client.get(url);
  }

  async post(url: string, data?: any) {
    return this.client.post(url, data);
  }

  async put(url: string, data?: any) {
    return this.client.put(url, data);
  }

  async delete(url: string) {
    return this.client.delete(url);
  }
}

export const apiClient = new ApiClient();