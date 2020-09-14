import {createSlice} from "@reduxjs/toolkit";
import {saveMetadata, saveReports} from "../../app/db";
import {isRejectedReport, PMCReportStatus} from "../../app/constants";
import {selectLogin} from "../login/loginSlice";
import {synchronize} from "../../app/sync";
import {loadSuccess as metadataLoadSuccess, selectMetadata} from "../../features/pmc/metadataSlice";

export const reportsStateFromReports = reports => {
    if (reports === undefined) {
        return undefined
    }
    let nextInternalId = 0

    const byId = reports.reduce((byId, r) => {
        r.internalId = nextInternalId++
        byId[r.internalId] = r
        return byId
    }, {})

    return {
        reports: byId,
        synchronizing: false,
        nextInternalId: nextInternalId
    }
}

export const pmcReportsSlice = createSlice({
    name: 'pmcReports',
    initialState: {
        reports: {},
        synchronizing: false,
        nextInternalId: 0
    },
    reducers: {
        replaceReports: (state, action) => {
            const newState = reportsStateFromReports(action.payload || [])
            state.reports = newState.reports
            state.synchronizing = newState.synchronizing
            state.nextInternalId = newState.nextInternalId
        },
        replaceReport: (state, action) => {
            const originalReport = action.payload;
            const report = {
                ...originalReport,
                internalId: isNaN(originalReport.internalId)
                    ? state.nextInternalId++
                    : originalReport.internalId
            };
            state.reports[report.internalId] = report;
        },
        deleteReport: (state, action) => {
            delete state.reports[action.payload];
        },
        submitReport: (state, action) => {
            const originalReport = action.payload;
            const report = {
                ...originalReport,
                status: PMCReportStatus.SUBMITTED_PENDING
            };
            state.reports[report.internalId] = report;
        },
        revertToDraft: (state, action) => {
            state.reports[action.payload].status = PMCReportStatus.DRAFT;
        },
        synchronizationStarted: (state, action) => {
            state.synchronizing = true;
        },
        synchronizationSuccess: (state, action) => {
            const existingReports = action.payload.existingReports

            const idToInternalId = Object.values(state.reports).reduce((m, r) => {
                if (!isNaN(r.id)) {
                    m[r.id] = r.internalId
                }
                return m
            }, {})

            existingReports.forEach(report => {
                const internalId = isNaN(idToInternalId[report.id])
                    ? state.nextInternalId++
                    : idToInternalId[report.id]

                delete idToInternalId[report.id]

                if (!(isRejectedReport(report) && isRejectedReport(state.reports[internalId]))) {
                    state.reports[internalId] = {
                        ...report,
                        internalId: internalId
                    }
                }
            })

            Object.values(idToInternalId).forEach(internalId => delete state.reports[internalId])

            action.payload.savedReports.forEach(r => state.reports[r.internalId] = r);

            state.synchronizing = false
        },
        synchronizationFailure: (state, action) => {
            console.log(`synchronizationFailure ${action.payload}`)
            state.synchronizing = false
        }
    }
});

const {
    replaceReport, deleteReport, submitReport, revertToDraft,
    synchronizationStarted, synchronizationSuccess, synchronizationFailure
} = pmcReportsSlice.actions;

export const { replaceReports } = pmcReportsSlice.actions;

export const selectPMCReports = state => state.pmcReports;

export const selectPMCReportsArray = state => selectPMCReportsWithFilter(state, _ => true)

export const selectPMCReportsForSync = state => selectPMCReportsWithFilter(state,
        r => r.status === PMCReportStatus.SUBMITTED_PENDING)

const selectPMCReportsWithFilter = (state, filter) => Object.values(state.pmcReports.reports).filter(filter)

export const performReplacePMCReport = report => createPersistingThunk(
    dispatch => dispatch(replaceReport(report)));

export const performDeletePMCReport = internalId => createPersistingThunk(
    dispatch => dispatch(deleteReport(internalId)));

export const performSubmitPMCReport = report => createPersistingThunk(
    dispatch => dispatch(submitReport(report)));

// TODO this action must not succeed if sync is in progress
export const performRevertPMCReportToDraft = internalId => createPersistingThunk(
    dispatch => dispatch(revertToDraft(internalId)));

const createPersistingThunk = cb => (dispatch, getState) => {
    cb(dispatch, getState);

    const state = getState()
    const login = selectLogin(state)
    const reports = selectPMCReportsArray(state);

    saveReports(login.user.id, reports)

    performSynchronization()(dispatch, getState)
}

export const performSynchronization = () => (dispatch, getState) => {
    dispatch(synchronizationStarted)

    const state = getState()
    const login = selectLogin(state)
    const reportsToSync = selectPMCReportsForSync(state)

    synchronize(login.user.id, login.user.token, reportsToSync).then(
        r => {
            dispatch(metadataLoadSuccess(r.metadata))
            dispatch(synchronizationSuccess(r))

            let state2 = getState();

            const reports = selectPMCReportsArray(state2);
            saveReports(login.user.id, reports)

            const metadata = selectMetadata(state2)
            saveMetadata(login.user.id, metadata.ref)
        },
        e => dispatch(synchronizationFailure(e.toString())))
}

export default pmcReportsSlice.reducer;
