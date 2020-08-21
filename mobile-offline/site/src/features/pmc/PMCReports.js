import React from 'react';
import {useState, useEffect} from "react";
import {loadPMCReports} from "../../app/db";

export function PMCReports() {
    const [reports, setReports] = useState([]);
    const [error, setError] = useState();

    useEffect(() => {
        loadPMCReports().then(setReports, setError);
    }, []);

    if (error) {
        return (
            <div className="container-fluid">
                <div className="alert alert-danger mt-3" role="alert">{error.message}</div>
            </div>
        );
    } else if (reports.length === 0) {
        return <div />;
    } else {
        const rows = reports.map((report) => (
            <tr key={report.id}>
                <td>{report.department.label}</td>
                <td>{report.tender.tenderTitle}</td>
            </tr>
        ));

        return (
            <table className="table table-sm">
                <thead>
                <tr>
                    <th scope="col">Department</th>
                    <th scope="col">Tender Title</th>
                </tr>
                </thead>
                <tbody>
                {rows}
                </tbody>
            </table>
        );
    }
}
