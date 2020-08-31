import {createSlice} from "@reduxjs/toolkit";
import {loadPMCReports} from "../../app/db";

export const pmcReportsSlice = createSlice({
    name: 'pmcReports',
    initialState: {
        reports: null,
        error: null
    },
    reducers: {
        loadSuccess: (state, action) => {
            let reports = {};
            action.payload.forEach(r => reports[r.id] = r);
            state.reports = reports;
            state.error = null;
        },
        loadFailure: (state, action) => {
            state.reports = null;
            state.error = action.payload;
        }
    }
});

export const {loadSuccess, loadFailure} = pmcReportsSlice.actions;

export const preformLoadPMCReports = () => dispatch => {
    loadPMCReports().then(
        reports => dispatch(loadSuccess(reports)),
        e => dispatch(loadFailure(e)));
};

export const selectPMCReports = state => state.pmcReports;

export const selectPMCReportsArray = state => {
    let array = [];
    for (const [, report] of Object.entries(state.pmcReports.reports || {})) {
        array.push(report);
    }
    return array;
}

export default pmcReportsSlice.reducer;
