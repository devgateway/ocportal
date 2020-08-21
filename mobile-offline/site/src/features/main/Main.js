import React from 'react';
import {useSelector} from "react-redux";
import {Login} from "../login/Login";
import {selectLogin} from "../login/loginSlice";
import {PMCReports} from "../pmc/PMCReports";

export function Main() {
    const login = useSelector(selectLogin);

    if (login.authenticated === true) {
        return <PMCReports />;
    } else {
        return <Login />;
    }
}
