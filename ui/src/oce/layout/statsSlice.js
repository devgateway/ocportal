import {createAsyncThunk, createSlice} from '@reduxjs/toolkit';
import {fetch} from "../api/Api";

export const NOT_INITIALIZED = 'not-initialized';
export const LOADED = 'loaded';
export const LOADING = 'loading';

export const loadStats = createAsyncThunk(
  'stats/load',
  () => fetch('/makueni/contractStats'),
  {
    condition: (arg, {getState}) => getState().stats.status === NOT_INITIALIZED
  });

export const counterSlice = createSlice({
  name: 'stats',
  initialState: {
    status: NOT_INITIALIZED,
    totalContracts: 0,
    totalContractsAmount: 0
  },
  extraReducers: {
    [loadStats.fulfilled]: (state, action) => {
      state.status = LOADED;
      state.totalContracts = action.payload.count;
      state.totalContractsAmount = action.payload.value;
    },
    [loadStats.pending]: state => {
      state.status = LOADING;
    },
    [loadStats.rejected]: state => {
      state.status = NOT_INITIALIZED;
    }
  }
});

export const selectStats = state => state.stats;

export default counterSlice.reducer;
