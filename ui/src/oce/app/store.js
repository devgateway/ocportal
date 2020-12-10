import { configureStore } from '@reduxjs/toolkit';
import statsReducer from '../layout/statsSlice'
import fmReducer from '../fm/fmSlice'

export default configureStore({
  reducer: {
    stats: statsReducer,
    fm: fmReducer
  }
});
