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
            <div key={report.id} className="card mt-3">
                <div className="card-body">
                    <h5 className="card-title">{report.tender.tenderTitle}</h5>
                    <p className="card-text">Report date: {report.reportDate.toLocaleDateString()}
                      <span className="badge badge-secondary ml-1">{report.status}</span>
                    </p>
                </div>
            </div>
        ));

        return (
            <div className="container-fluid">
                {rows}
            </div>
        );
    }
}
