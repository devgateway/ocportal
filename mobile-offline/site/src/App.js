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
import {preformLoadMetadata, selectMetadata} from "./features/pmc/metadataSlice";
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
                        <WithMetadata>
                            <PMCReports />
                        </WithMetadata>
                    </PrivateRoute>
                    <PrivateRoute path={["/report/:internalId", "/report"]}>
                        <WithMetadata>
                            <EditReport />
                        </WithMetadata>
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

function WithMetadata(props) {
    const metadata = useSelector(selectMetadata);
    const dispatch = useDispatch();
    useEffect(() => {
        if (metadata.ref === null) {
            dispatch(preformLoadMetadata());
        }
    }, [metadata.ref, dispatch]);
    if (metadata.ref === null) {
        return <div>Loading...</div>;
    } else {
        return props.children;
    }
}

export default App;
