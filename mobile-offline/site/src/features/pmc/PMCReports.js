import React from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {Link, useHistory} from "react-router-dom";
import {selectPMCReportsArray} from "./pmcReportsSlice";
import {selectMetadata} from "./metadataSlice";
import {performLogout} from "../login/loginSlice";
import {PMCReportStatus} from "../../app/constants";

export function PMCReports() {
    const pmcReportArray = useSelector(selectPMCReportsArray);
    const history = useHistory();
    const dispatch = useDispatch();

    const metadata = useSelector(selectMetadata);
    const tendersById = metadata.refById["Tender"];

    const handleEditReport = report => e => {
        e.preventDefault();
        history.push("/report/" + report.internalId);
    }

    const handleLogout = e => {
        e.preventDefault()
        dispatch(performLogout())
    }

    const getStatusClassName = status => {
        switch (status) {
            case PMCReportStatus.APPROVED: return "badge-success"
            case PMCReportStatus.TERMINATED: return "badge-term"
            case PMCReportStatus.DRAFT:
            case PMCReportStatus.REJECTED:
                return "badge-danger"
            case PMCReportStatus.SUBMITTED:
            case PMCReportStatus.SUBMITTED_PENDING:
                return "badge-warning"
            default:
                return ""
        }
    }

    const getDerivedStatusName = report => {
        return report.status === PMCReportStatus.DRAFT && report.rejected ? PMCReportStatus.REJECTED : report.status
    }

    const rows = pmcReportArray.map((report) => (
        <div key={report.internalId} className="card mt-3" onClick={handleEditReport(report)}>
            <div className="card-body">
                <h5 className="card-title">{(tendersById[report.tenderId] || {}).label
                    || <span className="text-muted">Tender not selected</span>}</h5>
                <p className="card-text">Report date: {report.reportDate
                    || <span className="text-muted">N/A</span>}
                  <span className={"badge ml-1 float-right " + getStatusClassName(getDerivedStatusName(report))}>
                      {getDerivedStatusName(report)}
                  </span>
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
