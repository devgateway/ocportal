import {createSlice} from '@reduxjs/toolkit';
import {loginUser} from "../../api/Api";
import {loadReports, saveUser, deleteUser} from "../../app/db";
import {replaceReports} from "../pmc/pmcReportsSlice";

export const loginStateFromUser = user => {
    if (user === undefined) {
        return undefined
    }
    return {
        authenticated: true,
        tokenValid: true,
        user: user
    }
}

export const loginSlice = createSlice({
    name: 'login',
    initialState: {
        authenticated: false,
        tokenValid: false,
        user: {},
        loading: false
    },
    reducers: {
        logout: (state, action) => {
            state.authenticated = false;
            state.user = {};
        },
        loginInvoked: (state, action) => {
            state.loading = true;
            state.error = null;
        },
        loginFailure: (state, action) => {
            state.authenticated = false;
            state.loading = false;
            state.error = action.payload;
        },
        loginSuccess: (state, action) => {
            state.loading = false;
            state.user = action.payload;
            state.authenticated = true;
            state.tokenValid = true;
            state.error = null;
        }
    },
});

const {logout, loginSuccess, loginInvoked, loginFailure} = loginSlice.actions

/**
 * Login a user using (username, password) tuple.
 */
export const performLogin = userPass => dispatch => {
    dispatch(loginInvoked(userPass));

    loginUser(userPass)
        .then(resolve => {
            const {data} = resolve;
            if (data === '') {
                dispatch(loginFailure("Login failed. Not a PMC user!"));
            } else {
                dispatch(loginSuccess(data));
                saveUser(data);
                dispatch(replaceReports(loadReports(data.id)))
            }
        })
        .catch(error => {
            let message;
            if (error.response) {
                message = "Login failed. Please check your credentials."
            } else if (error.request) {
                message = "Network error. Please check your internet connection."
            } else {
                message = "Unknown error."
            }
            dispatch(loginFailure(message));
        });
};

export const performLogout = () => dispatch => {
    deleteUser()
    dispatch(logout())
};

export const selectLogin = state => state.login;

export default loginSlice.reducer;
