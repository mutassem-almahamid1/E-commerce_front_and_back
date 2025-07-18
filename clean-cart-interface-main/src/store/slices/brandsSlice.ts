import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import { Brand } from '@/types';
import { apiClient } from '@/lib/api';

interface BrandsState {
  brands: Brand[];
  status: 'idle' | 'loading' | 'succeeded' | 'failed';
  error: string | null;
}

const initialState: BrandsState = {
  brands: [],
  status: 'idle',
  error: null,
};

export const fetchActiveBrands = createAsyncThunk(
  'brands/fetchActiveBrands',
  async (_, { rejectWithValue }) => {
    try {
      const brands = await apiClient.getActiveBrands();
      return brands;
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || 'Failed to fetch brands');
    }
  }
);

const brandsSlice = createSlice({
  name: 'brands',
  initialState,
  reducers: {
    clearError: (state) => {
      state.error = null;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchActiveBrands.pending, (state) => {
        state.status = 'loading';
        state.error = null;
      })
      .addCase(fetchActiveBrands.fulfilled, (state, action: PayloadAction<Brand[]>) => {
        state.status = 'succeeded';
        state.brands = action.payload;
      })
      .addCase(fetchActiveBrands.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.payload as string;
      });
  },
});

export const { clearError } = brandsSlice.actions;
export default brandsSlice.reducer;
