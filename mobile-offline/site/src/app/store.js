import { configureStore } from '@reduxjs/toolkit';
import counterReducer from '../features/counter/counterSlice';
import loginReducer from '../features/login/loginSlice';
import pmcReportsReducer from '../features/pmc/pmcReportsSlice';
import metadataReducer from '../features/pmc/metadataSlice';

export default configureStore({
  reducer: {
    counter: counterReducer,
    login: loginReducer,
    pmcReports: pmcReportsReducer,
    metadata: metadataReducer
  },
});
