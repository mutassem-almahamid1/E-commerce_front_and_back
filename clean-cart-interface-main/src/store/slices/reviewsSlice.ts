import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import { Review, ReviewRequest } from '@/types';
import { apiClient } from '@/lib/api';

interface ReviewsState {
  reviews: Review[];
  status: 'idle' | 'loading' | 'succeeded' | 'failed';
  submitStatus: 'idle' | 'loading' | 'succeeded' | 'failed';
  error: string | null;
}

const initialState: ReviewsState = {
  reviews: [],
  status: 'idle',
  submitStatus: 'idle',
  error: null,
};

export const fetchReviewsByProductId = createAsyncThunk(
  'reviews/fetchReviewsByProductId',
  async (productId: string, { rejectWithValue }) => {
    try {
      const reviews = await apiClient.getReviewsByProductId(productId);
      return reviews;
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || 'Failed to fetch reviews');
    }
  }
);

export const submitReview = createAsyncThunk(
  'reviews/submitReview',
  async (reviewData: ReviewRequest & { userId: string }, { rejectWithValue }) => {
    try {
      const review = await apiClient.submitReview(reviewData);
      return review;
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || 'Failed to submit review');
    }
  }
);

const reviewsSlice = createSlice({
  name: 'reviews',
  initialState,
  reducers: {
    clearError: (state) => {
      state.error = null;
    },
    resetSubmitStatus: (state) => {
      state.submitStatus = 'idle';
    },
  },
  extraReducers: (builder) => {
    builder
      // Fetch reviews
      .addCase(fetchReviewsByProductId.pending, (state) => {
        state.status = 'loading';
        state.error = null;
      })
      .addCase(fetchReviewsByProductId.fulfilled, (state, action: PayloadAction<Review[]>) => {
        state.status = 'succeeded';
        state.reviews = action.payload;
      })
      .addCase(fetchReviewsByProductId.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.payload as string;
      })
      // Submit review
      .addCase(submitReview.pending, (state) => {
        state.submitStatus = 'loading';
        state.error = null;
      })
      .addCase(submitReview.fulfilled, (state, action: PayloadAction<Review>) => {
        state.submitStatus = 'succeeded';
        state.reviews.unshift(action.payload); // Add new review to the beginning
      })
      .addCase(submitReview.rejected, (state, action) => {
        state.submitStatus = 'failed';
        state.error = action.payload as string;
      });
  },
});

export const { clearError, resetSubmitStatus } = reviewsSlice.actions;
export default reviewsSlice.reducer;
