import React, {useEffect} from 'react';
import {
    BrowserRouter as Router,
    Switch,
    Route,
    Redirect
} from "react-router-dom";

import {Login} from "./features/login/Login";
import {PMCReports} from "./features/pmc/PMCReports";
import {EditReport} from "./features/pmc/EditReport";

import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import {useDispatch, useSelector} from "react-redux";
import {selectLogin} from "./features/login/loginSlice";
import {performSynchronization} from "./features/pmc/pmcReportsSlice";

function App() {
    const dispatch = useDispatch()
    const login = useSelector(selectLogin)

    useEffect(() => {
        if (login.authenticated) {
            dispatch(performSynchronization())
        }
    }, [dispatch, login.authenticated])

    return (
        <Router>
            <div className="App">
                <header>

                </header>

                <Switch>
                    <Route path="/login">
                        <Login />
                    </Route>
                    <PrivateRoute exact path="/">
                        <PMCReports />
                    </PrivateRoute>
                    <PrivateRoute path={["/report/:internalId", "/report"]}>
                        <EditReport />
                    </PrivateRoute>
                </Switch>
            </div>
        </Router>
    );
}

function PrivateRoute({ children, ...rest }) {
    const login = useSelector(selectLogin);
    return (
        <Route
            {...rest}
            render={({ location }) =>
                login.authenticated ? (
                    children
                ) : (
                    <Redirect
                        to={{
                            pathname: "/login",
                            state: { from: location }
                        }}
                    />
                )
            }
        />
    );
}

export default App;
