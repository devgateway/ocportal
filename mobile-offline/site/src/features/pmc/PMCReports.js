import React from 'react';
import {useSelector, useDispatch} from 'react-redux';
import {useEffect} from "react";
import {selectPMCReports, preformLoadPMCReports, addReport} from "./pmcReportsSlice";
import {EditReport} from "./EditReport";
import {preformLoadMetadata} from "./metadataSlice";

export function PMCReports() {
    const pmcReports = useSelector(selectPMCReports);
    const dispatch = useDispatch();

    useEffect(() => {
        dispatch(preformLoadPMCReports());
        dispatch(preformLoadMetadata());
    }, [dispatch]);

    const handleAddReport = (e) => {
        e.preventDefault();
        dispatch(addReport());
    };

    if (pmcReports.editingReport !== null) {
        return <EditReport />;
    } else if (pmcReports.error) {
        return (
            <div className="container-fluid">
                <div className="alert alert-danger mt-3" role="alert">{pmcReports.error.message}</div>
            </div>
        );
    } else if (pmcReports.reports === null) {
        return <div />;
    } else {
        const rows = pmcReports.reports.map((report) => (
            <div key={report.id} className="card mt-3">
                <div className="card-body">
                    <h5 className="card-title">{report.tender.tenderTitle}</h5>
                    <p className="card-text">Report date: {report.reportDate}
                      <span className="badge badge-secondary ml-1">{report.status}</span>
                    </p>
                </div>
            </div>
        ));

        return (
            <div className="container-fluid pt-3 pb-3">
                <button onClick={handleAddReport} className="btn btn-success">Add report</button>
                {rows}
            </div>
        );
    }
}
