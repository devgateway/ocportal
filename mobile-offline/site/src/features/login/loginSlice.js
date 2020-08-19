import {createSlice} from '@reduxjs/toolkit';
import {loginUser} from "../../api/Api";

export const loginSlice = createSlice({
    name: 'login',
    initialState: {
        authenticated: false,
        user: {},
        loading: false
    },
    reducers: {
        loginInvoked: (state, action) => {
            state.loading = true;
            console.log("Login was invoked for " + JSON.stringify(action.payload));
        },
        loginFailure: (state, action) => {
            console.log("Login failed: " + JSON.stringify(action.payload));
            state.authenticated = false;
            state.loading = false;
        },
        loginSuccess: (state, action) => {
            console.log("Login success: " + JSON.stringify(action.payload));
            state.loading = false;
            state.user = action.payload;
            state.authenticated = true;
        },
    },
});

export const {loginInvoked, loginFailure, loginSuccess} = loginSlice.actions;

/**
 * Login a user using (username, password) tuple.
 */
export const performLogin = userPass => dispatch => {
    dispatch(loginInvoked(userPass));
    setTimeout(() => {
        loginUser(userPass)
            .then(resolve => {
                const {data} = resolve;
                // check if the user is authenticated and save it's details
                if (data.token) {
                    dispatch(loginSuccess(data));
                    console.log("User authenticated");
                } else {
                    dispatch(loginFailure("Username or password is incorrect!"));
                }
            })
            .catch(reject => {
                console.log(reject);
                dispatch(loginFailure("Network Error! Ensure Internet is up and retry!"));
            });
    }, 3000); //simulate network delay
};

export const selectLogin = state => state.login;

export default loginSlice.reducer;
