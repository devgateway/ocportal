import React, {useEffect, useState} from 'react';
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
import {clearSubmittedOffline, performSynchronization, selectPMCReports} from "./features/pmc/pmcReportsSlice";
import {SYNC_INTERVAL} from "./app/constants";
import {RecoverPwd} from "./features/login/RecoverPwd";
import {ChangePwd} from "./features/login/ChangePwd";
import {Alert} from "reactstrap";

function App() {

    useSync()

    return (
        <Router>
            <ScrollToTop />

            <OfflineMessage />

            <div className="App">
                <header>

                </header>

                <Switch>
                    <Route path="/login">
                        <Login />
                    </Route>
                    <Route path="/recoverPwd">
                        <RecoverPwd />
                    </Route>
                    <Route path="/changePwd">
                        <ChangePwd />
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

function OfflineMessage() {
    const pmcReports = useSelector(selectPMCReports)
    const dispatch = useDispatch()

    useEffect(() => {
        let timeout = null
        if (pmcReports.submittedOffline) {
            timeout = setTimeout(() => {
                dispatch(clearSubmittedOffline())
            }, 10000)
        }
        return () => {
            if (timeout !== null) {
                clearTimeout(timeout)
            }
        }
    }, [dispatch, pmcReports.submittedOffline])

    if (pmcReports.submittedOffline) {
        return (
            <div style={{position: 'fixed', bottom: 15, right: 15, left: 15, zIndex: 1050}}>
                <Alert color="info" className="mb-0">
                    Offline. Report will be submitted later.
                </Alert>
            </div>
        )
    } else {
        return null
    }
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
                dispatch(performSynchronization())
            }
        }

        sync()

        const intervalId = setInterval(sync, SYNC_INTERVAL)

        return () => clearInterval(intervalId)
    }, [dispatch, login.authenticated])

    const [lastSyncTime, setLastSyncTime] = useState(null)

    useEffect(() => {
        const onlineEventListener = () => {
            let now = Date.now()
            if (login.authenticated && (!lastSyncTime || lastSyncTime + SYNC_INTERVAL < now)) {
                setLastSyncTime(now)
                dispatch(performSynchronization())
            }
        }

        window.addEventListener('online', onlineEventListener)

        return () => window.removeEventListener('online', onlineEventListener)
    }, [dispatch, lastSyncTime, login.authenticated])
}

export default App;
