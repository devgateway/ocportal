import React from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {Link, useHistory} from "react-router-dom";
import {selectPMCReportsArray} from "./pmcReportsSlice";
import {selectMetadata} from "./metadataSlice";
import {performLogout} from "../login/loginSlice";

export function PMCReports() {
    const pmcReportArray = useSelector(selectPMCReportsArray);
    const history = useHistory();
    const dispatch = useDispatch();

    const metadata = useSelector(selectMetadata);
    const tendersById = metadata.ref["TenderById"];

    const handleEditReport = report => e => {
        e.preventDefault();
        history.push("/report/" + report.internalId);
    }

    const handleLogout = e => {
        e.preventDefault()
        dispatch(performLogout())
    }

    const rows = pmcReportArray.map((report) => (
        <div key={report.internalId} className="card mt-3" onClick={handleEditReport(report)}>
            <div className="card-body">
                <h5 className="card-title">{(tendersById[report.tenderId] || {}).label}</h5>
                <p className="card-text">Report date: {report.reportDate || 'N/A'}
                  <span className="badge badge-secondary ml-1">{report.status}</span>
                </p>
            </div>
        </div>
    ));

    return (
        <div className="container-fluid pt-3 pb-3">
            <div>
            <Link to="/report" className="btn btn-success">Add report</Link>

            <button type='button' className="btn btn-secondary float-right" onClick={handleLogout}>Logout</button>
            </div>

            {rows}

            {pmcReportArray.length === 0 ? <div className='text-muted text-center mt-3'>No reports.</div> : null}
        </div>
    );
}
