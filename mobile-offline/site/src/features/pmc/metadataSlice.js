import {createSlice} from "@reduxjs/toolkit";
import {loadMetadata} from "../../app/db";

export const metadataSlice = createSlice({
    name: 'metadata',
    initialState: {
        error: null,
        ref: null
    },
    reducers: {
        loadSuccess: (state, action) => {
            state.ref = action.payload;
            state.error = null;
        },
        loadFailure: (state, action) => {
            state.ref = null;
            state.error = action.payload;
        }
    }
});

export const {loadSuccess, loadFailure} = metadataSlice.actions;

export const preformLoadMetadata = () => dispatch => {
    loadMetadata().then(d => dispatch(loadSuccess(d)), e => dispatch(loadFailure(e)));
};

export const selectMetadata = state => state.metadata;

export default metadataSlice.reducer;
