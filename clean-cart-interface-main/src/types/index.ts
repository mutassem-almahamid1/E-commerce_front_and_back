// User types
export interface User {
  id: string;
  username: string;
  email: string;
  image?: string;
  role: 'USER' | 'ADMIN';
  location: string;
  status: 'ACTIVE' | 'BLOCKED';
  createdAt: string;
  updatedAt: string;
}

export interface UserSignUpRequest {
  username: string;
  email: string;
  password: string;
  image?: string;
  location: string;
}

export interface UserLoginRequest {
  email: string;
  password: string;
}

export interface JwtResponse {
  token: string;
  refreshToken: string;
  id: string;
  username: string;
  email: string;
  roles: string[];
}

// Product types
export interface Product {
  id: string;
  name: string;
  description: string;
  price: number;
  image?: string;            // fallback for single image
  images: string[];          // list of images from backend
  brand: string;             // Changed from Brand object to string
  categoryId: string;        // Changed from Category object to categoryId
  stockQuantity: number;     // Changed from stock to stockQuantity to match backend
  avgRating: number;         // Changed from rating to avgRating to match backend
  reviewCount: number;
  technicalSpecs?: Record<string, string>; // Changed from specifications to technicalSpecs
  status: string;            // Added status field
  orderStatus: string;        // Added itemStatus field
  createdAt: string;
  updatedAt: string;
  deletedAt?: string;
}

export interface ProductRequest {
  name: string;
  description: string;
  price: number;
  image: string;
  images?: string[];
  brandId: string;
  categoryId: string;
  stock: number;
  specifications?: Record<string, string>;
}

// Category types
export interface Category {
  id: string;
  categoryName: string;
  categoryDescription?: string;
  categoryImage?: string;
  status: string;
  createdAt: string;
  updatedAt: string;
  deletedAt?: string;
}

export interface CategoryRequest {
  categoryName: string;
  categoryDescription?: string;
  categoryImage?: string;
  status: string;
}

// Brand types
export interface Brand {
  id: string;
  name: string;
  description?: string;
  logo?: string;
  active: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface BrandRequest {
  name: string;
  description?: string;
  logo?: string;
  active: boolean;
}

// Review types
export interface Review {
  id: string;
  rating: number;
  comment: string;
  user: User;
  product: Product;
  createdAt: string;
  updatedAt: string;
}

export interface ReviewRequest {
  productId: string;
  rating: number;
  comment: string;
}

// API Response types
export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}

export interface ApiError {
  message: string;
  status: number;
  timestamp: string;
}

// Cart types
export interface CartItem {
  productId: string;
  quantity: number;
  price: number;
  product?: Product; // للعرض في الواجهة
}

export interface Cart {
  id?: string;
  userId: string;
  cartItems: CartItem[];
  totalAmount: number;
  createdAt?: string;
  updatedAt?: string;
}

export interface CartState {
  items: Array<{
    product: Product;
    quantity: number;
  }>;
  total: number;
  itemCount: number;
}

// Order types
export interface OrderItem {
  productId: string;
  productName: string;
  imageUrl?: string;
  quantity: number;
  price: number;
  totalPrice: number;
  status: 'PENDING' | 'CONFIRMED' | 'SHIPPED' | 'DELIVERED' | 'CANCELLED' | 'RETURNED';
  productDescription?: string;
  brandName?: string;
  categoryName?: string;
}

export interface OrderItemRequest {
  productId: string;
  quantity: number;
}

export interface Address {
  id?: string;
  street: string;
  city: string;
  state: string;
  zipCode: string;
  country: string;
  isDefault?: boolean;
}

export interface Order {
  id: string;
  userId: string;
  orderNumber: string;
  shippingAddress: Address;
  billingAddress?: Address;
  totalAmount: number;
  shippingCost: number;
  taxAmount: number;
  discountAmount: number;
  finalAmount: number;
  paymentMethod: 'CREDIT_CARD' | 'DEBIT_CARD' | 'PAYPAL' | 'CASH_ON_DELIVERY';
  paymentId?: string;
  isPaid: boolean;
  items: OrderItem[];
  status: 'PENDING' | 'CONFIRMED' | 'PROCESSING' | 'SHIPPED' | 'DELIVERED' | 'CANCELLED' | 'RETURNED';
  notes?: string;
  trackingNumber?: string;
  createdAt: string;
  updatedAt?: string;
  shippedAt?: string;
  deliveredAt?: string;
  totalItems: number;
  statusDescription: string;
}

export interface OrderRequest {
  items: OrderItemRequest[];
  shippingAddress: Address;
  billingAddress?: Address;
  paymentMethod: 'CREDIT_CARD' | 'DEBIT_CARD' | 'PAYPAL' | 'CASH_ON_DELIVERY';
  notes?: string;
  couponCode?: string;
}

export interface OrderSummary {
  totalOrders: number;
  pendingOrders: number;
  completedOrders: number;
  totalRevenue: number;
}
