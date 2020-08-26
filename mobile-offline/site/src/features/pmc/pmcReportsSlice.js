import {createSlice} from "@reduxjs/toolkit";
import {loadPMCReports} from "../../app/db";

export const pmcReportsSlice = createSlice({
    name: 'pmcReports',
    initialState: {
        reports: [],
        editingReport: null,
        error: null,
        viewReportId: null
    },
    reducers: {
        loadSuccess: (state, action) => {
            state.reports = action.payload;
            state.error = null;
        },
        loadFailure: (state, action) => {
            state.reports = [];
            state.error = action.payload;
        },
        addReport: (state, action) => {
            state.editingReport = {};
        },
        cancelEditReport: (state, action) => {
            state.editingReport = null;
        }
    }
});

export const {loadSuccess, loadFailure, addReport, cancelEditReport} = pmcReportsSlice.actions;

export const preformLoadPMCReports = () => dispatch => {
    loadPMCReports().then(d => dispatch(loadSuccess(d)), e => dispatch(loadFailure(e)));
};

export const selectPMCReports = state => state.pmcReports;

export default pmcReportsSlice.reducer;
