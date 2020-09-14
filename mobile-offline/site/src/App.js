import React, {useEffect} from 'react';
import {
    HashRouter as Router,
    Switch,
    Route,
    Redirect,
    useLocation
} from "react-router-dom";

import {Login} from "./features/login/Login";
import {PMCReports} from "./features/pmc/PMCReports";
import {EditReport} from "./features/pmc/EditReport";

import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import {useDispatch, useSelector} from "react-redux";
import {selectLogin} from "./features/login/loginSlice";
import {performSynchronization} from "./features/pmc/pmcReportsSlice";
import {SYNC_INTERVAL} from "./app/constants";

function App() {

    useSync()

    return (
        <Router>
            <ScrollToTop />
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

function ScrollToTop() {
    const { pathname } = useLocation();

    useEffect(() => {
        window.scrollTo(0, 0);
    }, [pathname]);

    return null;
}

function useSync() {
    const dispatch = useDispatch()
    const login = useSelector(selectLogin)

    useEffect(() => {
        const sync = () => {
            if (login.authenticated) {
                console.log('sync!')
                dispatch(performSynchronization())
            }
        }

        sync()

        const intervalId = setInterval(sync, SYNC_INTERVAL)
        return () => {
            console.log('cleared sync!')
            clearInterval(intervalId)
        }
    }, [dispatch, login.authenticated])
}

export default App;
