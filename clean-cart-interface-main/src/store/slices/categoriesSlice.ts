import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import { Category } from '@/types';
import { apiClient } from '@/lib/api';

interface CategoriesState {
  categories: Category[];
  status: 'idle' | 'loading' | 'succeeded' | 'failed';
  error: string | null;
}

const initialState: CategoriesState = {
  categories: [],
  status: 'idle',
  error: null,
};

export const fetchActiveCategories = createAsyncThunk(
  'categories/fetchActiveCategories',
  async (_, { rejectWithValue }) => {
    try {
      const categories = await apiClient.getActiveCategories();
      return categories;
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || 'Failed to fetch categories');
    }
  }
);

const categoriesSlice = createSlice({
  name: 'categories',
  initialState,
  reducers: {
    clearError: (state) => {
      state.error = null;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchActiveCategories.pending, (state) => {
        state.status = 'loading';
        state.error = null;
      })
      .addCase(fetchActiveCategories.fulfilled, (state, action: PayloadAction<Category[]>) => {
        state.status = 'succeeded';
        state.categories = action.payload;
      })
      .addCase(fetchActiveCategories.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.payload as string;
      });
  },
});

export const { clearError } = categoriesSlice.actions;
export default categoriesSlice.reducer;
