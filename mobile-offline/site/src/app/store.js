import { configureStore } from '@reduxjs/toolkit';
import counterReducer from '../features/counter/counterSlice';
import loginReducer, {loginStateFromUser} from '../features/login/loginSlice';
import pmcReportsReducer, {reportsStateFromReports} from '../features/pmc/pmcReportsSlice';
import metadataReducer from '../features/pmc/metadataSlice';
import {loadUser, loadReports} from "./db";

const login = loginStateFromUser(loadUser())

const reports = login ? reportsStateFromReports(loadReports(login.user.id)) : undefined

const preloadedState = {
  login: login,
  pmcReports: reports
}

export default configureStore({
  reducer: {
    counter: counterReducer,
    login: loginReducer,
    pmcReports: pmcReportsReducer,
    metadata: metadataReducer
  },
  preloadedState
});
