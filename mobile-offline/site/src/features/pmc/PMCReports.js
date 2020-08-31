import React from 'react';
import {useSelector} from 'react-redux';
import {Link, useHistory} from "react-router-dom";
import {selectPMCReports, selectPMCReportsArray} from "./pmcReportsSlice";

export function PMCReports() {
    const pmcReports = useSelector(selectPMCReports);
    const pmcReportArray = useSelector(selectPMCReportsArray);
    const history = useHistory();

    const handleEditReport = report => e => {
        e.preventDefault();
        history.push("/report/" + report.id);
    }

    if (pmcReports.error) {
        return (
            <div className="container-fluid">
                <div className="alert alert-danger mt-3" role="alert">{pmcReports.error.message}</div>
            </div>
        );
    } else if (pmcReportArray === null) {
        return <div />;
    } else {
        const rows = pmcReportArray.map((report) => (
            <div key={report.id} className="card mt-3" onClick={handleEditReport(report)}>
                <div className="card-body">
                    <h5 className="card-title">{report.tenderId}</h5>
                    <p className="card-text">Report date: {report.reportDate}
                      <span className="badge badge-secondary ml-1">{report.status}</span>
                    </p>
                </div>
            </div>
        ));

        return (
            <div className="container-fluid pt-3 pb-3">
                <Link to="/report" className="btn btn-success">Add report</Link>
                {rows}
            </div>
        );
    }
}
