import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { fetch } from '../api/Api';

export const NOT_INITIALIZED = 'not-initialized';
export const LOADED = 'loaded';
export const LOADING = 'loading';

export const loadFM = createAsyncThunk(
  'fm/load',
  () => fetch('/fm/featureProperties', {
    fmPrefixes: ['viz.', 'publicView'],
  }),
  {
    condition: (arg, { getState }) => getState().fm.status === NOT_INITIALIZED,
  },
);

export const fmSlice = createSlice({
  name: 'fm',
  initialState: {
    status: NOT_INITIALIZED,
    list: 0,
  },
  extraReducers: {
    [loadFM.fulfilled]: (state, action) => {
      state.status = LOADED;
      state.list = action.payload
        .filter((fm) => fm.visible)
        .map((fm) => fm.name);
    },
    [loadFM.pending]: (state) => {
      state.status = LOADING;
    },
    [loadFM.rejected]: (state) => {
      state.status = NOT_INITIALIZED;
    },
  },
});

export const selectFM = (state) => state.fm;

export default fmSlice.reducer;
