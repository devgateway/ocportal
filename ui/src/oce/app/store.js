import { configureStore } from '@reduxjs/toolkit';
import statsReducer from '../layout/statsSlice'

export default configureStore({
  reducer: {
    stats: statsReducer
  }
});
