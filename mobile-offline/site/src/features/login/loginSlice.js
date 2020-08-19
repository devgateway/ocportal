import { createSlice } from '@reduxjs/toolkit';
import {isAuthenticated, loginUser} from "../../api/Api";

export const loginSlice = createSlice({
  name: 'login',
  initialState: {
    authenticated: false,
    user: {}
  },
  reducers: {
    loginInvoked: (state, action) => {
      console.log("Login was invoked for"+ JSON.stringify(action.payload));
    },
    loginFailure: (state, action) => {
        console.log("Login failed: "+ JSON.stringify(action.payload));
      state.authenticated = false;
    },
    loginSuccess: (state, action) => {
        console.log("Login success: "+ JSON.stringify(action.payload));
      state.user = action.payload;
      state.authenticated = true;
    },
  },
});

export const { loginInvoked, loginFailure, loginSuccess } = loginSlice.actions;

/**
 * Login a user using (username, password) tuple.
 */
export const performLogin = userPass => dispatch => {
  dispatch(loginInvoked(userPass));

  //this is in case we use form content disposition, not json:
  //const data = new FormData();
  // data.append('username', userPass.username);
  // data.append('password', userPass.password);

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
};



// The function below is called a selector and allows us to select a value from
// the state. Selectors can also be defined inline where they're used instead of
// in the slice file. For example: `useSelector((state) => state.counter.value)`
export const selectLogin = state => state.login;

export default loginSlice.reducer;
