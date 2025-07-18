import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import { Product, Page } from '@/types';
import { apiClient } from '@/lib/api';

interface ProductsState {
  products: Product[];
  selectedProduct: Product | null;
  newestProducts: Product[];
  topRatedProducts: Product[];
  totalPages: number;
  currentPage: number;
  status: 'idle' | 'loading' | 'succeeded' | 'failed';
  error: string | null;
}

const initialState: ProductsState = {
  products: [],
  selectedProduct: null,
  newestProducts: [],
  topRatedProducts: [],
  totalPages: 0,
  currentPage: 0,
  status: 'idle',
  error: null,
};

// Async thunks
export const fetchProducts = createAsyncThunk(
  'products/fetchProducts',
  async ({ page = 0, size = 10 }: { page?: number; size?: number } = {}, { rejectWithValue }) => {
    try {
      const response = await apiClient.getProducts(page, size);
      return response;
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || 'Failed to fetch products');
    }
  }
);

export const fetchProductById = createAsyncThunk(
  'products/fetchProductById',
  async (id: string, { rejectWithValue }) => {
    try {
      const product = await apiClient.getProductById(id);
      return product;
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || 'Failed to fetch product');
    }
  }
);

export const fetchNewestProducts = createAsyncThunk(
  'products/fetchNewestProducts',
  async (_, { rejectWithValue }) => {
    try {
      const products = await apiClient.getNewestProducts();
      return products;
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || 'Failed to fetch newest products');
    }
  }
);

export const fetchTopRatedProducts = createAsyncThunk(
  'products/fetchTopRatedProducts',
  async (_, { rejectWithValue }) => {
    try {
      const products = await apiClient.getTopRatedProducts();
      return products;
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || 'Failed to fetch top rated products');
    }
  }
);

const productsSlice = createSlice({
  name: 'products',
  initialState,
  reducers: {
    clearSelectedProduct: (state) => {
      state.selectedProduct = null;
    },
    clearError: (state) => {
      state.error = null;
    },
  },
  extraReducers: (builder) => {
    builder
      // Fetch products
      .addCase(fetchProducts.pending, (state) => {
        state.status = 'loading';
        state.error = null;
      })
      .addCase(fetchProducts.fulfilled, (state, action: PayloadAction<Page<Product>>) => {
        state.status = 'succeeded';
        state.products = action.payload.content;
        state.totalPages = action.payload.totalPages;
        state.currentPage = action.payload.number;
      })
      .addCase(fetchProducts.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.payload as string;
      })
      // Fetch product by ID
      .addCase(fetchProductById.pending, (state) => {
        state.status = 'loading';
        state.error = null;
      })
      .addCase(fetchProductById.fulfilled, (state, action: PayloadAction<Product>) => {
        state.status = 'succeeded';
        state.selectedProduct = action.payload;
      })
      .addCase(fetchProductById.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.payload as string;
      })
      // Fetch newest products
      .addCase(fetchNewestProducts.fulfilled, (state, action: PayloadAction<Product[]>) => {
        state.newestProducts = action.payload;
      })
      // Fetch top rated products
      .addCase(fetchTopRatedProducts.fulfilled, (state, action: PayloadAction<Product[]>) => {
        state.topRatedProducts = action.payload;
      });
  },
});

export const { clearSelectedProduct, clearError } = productsSlice.actions;
export default productsSlice.reducer;
