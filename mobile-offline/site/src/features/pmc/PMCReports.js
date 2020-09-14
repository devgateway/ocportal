import React from 'react';
import {useSelector} from 'react-redux';
import {Link, useHistory} from "react-router-dom";
import {selectPMCReportsArray} from "./pmcReportsSlice";
import {selectMetadata} from "./metadataSlice";
import {isRejectedReport, PMCReportStatus} from "../../app/constants";
import {formatDateForDisplay, parseDate} from "../../app/date";
import {Border} from "./Border";

export function PMCReports() {
    const pmcReportArray = useSelector(selectPMCReportsArray);
    const history = useHistory();

    const metadata = useSelector(selectMetadata);
    const tendersById = metadata.refById["Tender"] || {};

    const handleEditReport = report => e => {
        e.preventDefault();
        history.push("/report/" + report.internalId);
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
        return isRejectedReport(report) ? PMCReportStatus.REJECTED : report.status
    }

    const rows = pmcReportArray.map((report) => (
        <div key={report.internalId} className="card mt-3" onClick={handleEditReport(report)}>
            <div className="card-body">
                <h5 className="card-title">{(tendersById[report.tenderId] || {}).label
                    || <span className="text-muted">Tender not selected</span>}</h5>
                <p className="card-text">Report date: {formatDateForDisplay(parseDate(report.reportDate))
                    || <span className="text-muted">N/A</span>}
                  <span className={"badge ml-1 float-right " + getStatusClassName(getDerivedStatusName(report))}>
                      {getDerivedStatusName(report)}
                  </span>
                </p>
            </div>
        </div>
    ));

    return (
        <Border title="Reports" extraNavBar={
            <Link to="/report" className="btn btn-success">Add report</Link>
        }>
            <div className="container-fluid pb-3">

                {rows}

                {pmcReportArray.length === 0 ? <div className='text-muted text-center mt-3'>No reports.</div> : null}
            </div>
        </Border>
    );
}
