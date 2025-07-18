import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import { CartState, Product, Cart, CartItem } from '@/types';
import { apiClient } from '@/lib/api';

// Initial state
const initialState: CartState = {
  items: [],
  total: 0,
  itemCount: 0,
};

// Async thunks for API calls
export const fetchCart = createAsyncThunk(
  'cart/fetchCart',
  async (userId: string) => {
    const response = await apiClient.getCart(userId);
    return response;
  }
);

export const addToCartAPI = createAsyncThunk(
  'cart/addToCartAPI',
  async ({ userId, cartItem }: { userId: string; cartItem: CartItem }) => {
    const response = await apiClient.addToCart(userId, cartItem);
    return response;
  }
);

export const removeFromCartAPI = createAsyncThunk(
  'cart/removeFromCartAPI',
  async ({ userId, productId }: { userId: string; productId: string }) => {
    const response = await apiClient.removeFromCart(userId, productId);
    return response;
  }
);

export const clearCartAPI = createAsyncThunk(
  'cart/clearCartAPI',
  async (userId: string) => {
    await apiClient.clearCart(userId);
  }
);

const cartSlice = createSlice({
  name: 'cart',
  initialState,
  reducers: {
    addToCart: (state, action: PayloadAction<{ product: Product; quantity?: number }>) => {
      const { product, quantity = 1 } = action.payload;
      const existingItem = state.items.find(item => item.product.id === product.id);

      if (existingItem) {
        existingItem.quantity += quantity;
      } else {
        state.items.push({ product, quantity });
      }

      // Recalculate totals
      state.itemCount = state.items.reduce((total, item) => total + item.quantity, 0);
      state.total = state.items.reduce((total, item) => total + (item.product.price * item.quantity), 0);
    },

    removeFromCart: (state, action: PayloadAction<string>) => {
      const productId = action.payload;
      state.items = state.items.filter(item => item.product.id !== productId);

      // Recalculate totals
      state.itemCount = state.items.reduce((total, item) => total + item.quantity, 0);
      state.total = state.items.reduce((total, item) => total + (item.product.price * item.quantity), 0);
    },

    updateQuantity: (state, action: PayloadAction<{ productId: string; quantity: number }>) => {
      const { productId, quantity } = action.payload;
      const item = state.items.find(item => item.product.id === productId);

      if (item) {
        if (quantity <= 0) {
          state.items = state.items.filter(item => item.product.id !== productId);
        } else {
          item.quantity = quantity;
        }
      }

      // Recalculate totals
      state.itemCount = state.items.reduce((total, item) => total + item.quantity, 0);
      state.total = state.items.reduce((total, item) => total + (item.product.price * item.quantity), 0);
    },

    clearCart: (state) => {
      state.items = [];
      state.total = 0;
      state.itemCount = 0;
    },

    // Load cart from API response
    loadCart: (state, action: PayloadAction<Cart>) => {
      const cart = action.payload;
      // تحويل بيانات السلة من الباك إند إلى format الفرونت إند
      state.items = cart.cartItems.map(item => ({
        product: item.product!,
        quantity: item.quantity
      }));
      state.total = cart.totalAmount;
      state.itemCount = cart.cartItems.reduce((total, item) => total + item.quantity, 0);
    }
  },

  extraReducers: (builder) => {
    builder
      .addCase(fetchCart.fulfilled, (state, action) => {
        const cart: Cart = action.payload;
        if (cart && cart.cartItems) {
          state.items = cart.cartItems.map(item => ({
            product: item.product!,
            quantity: item.quantity
          }));
          state.total = cart.totalAmount;
          state.itemCount = cart.cartItems.reduce((total, item) => total + item.quantity, 0);
        }
      })
      .addCase(addToCartAPI.fulfilled, (state, action) => {
        const cart: Cart = action.payload;
        if (cart && cart.cartItems) {
          state.items = cart.cartItems.map(item => ({
            product: item.product!,
            quantity: item.quantity
          }));
          state.total = cart.totalAmount;
          state.itemCount = cart.cartItems.reduce((total, item) => total + item.quantity, 0);
        }
      })
      .addCase(removeFromCartAPI.fulfilled, (state, action) => {
        const cart: Cart = action.payload;
        if (cart && cart.cartItems) {
          state.items = cart.cartItems.map(item => ({
            product: item.product!,
            quantity: item.quantity
          }));
          state.total = cart.totalAmount;
          state.itemCount = cart.cartItems.reduce((total, item) => total + item.quantity, 0);
        }
      })
      .addCase(clearCartAPI.fulfilled, (state) => {
        state.items = [];
        state.total = 0;
        state.itemCount = 0;
      });
  },
});

export const { addToCart, removeFromCart, updateQuantity, clearCart, loadCart } = cartSlice.actions;
export default cartSlice.reducer;
