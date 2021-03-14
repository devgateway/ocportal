import React from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {Link, useHistory} from "react-router-dom";
import {errorDialogClosed, selectPMCReports, selectPMCReportsArray, tokenDialogClosed} from "./pmcReportsSlice";
import {selectMetadata} from "./metadataSlice";
import {isRejectedReport, PMCReportStatus} from "../../app/constants";
import {formatDateForDisplay, parseDate} from "../../app/date";
import {Border} from "./Border";
import {Button, Modal, ModalBody, ModalFooter, ModalHeader} from "reactstrap";
import 'font-awesome/css/font-awesome.min.css';
import {performLogout} from "../login/loginSlice";

export function PMCReports() {
    const pmcReportArray = useSelector(selectPMCReportsArray);
    const history = useHistory();

    const metadata = useSelector(selectMetadata);
    const tendersById = metadata.refById["Tender"];

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
                <p className="card-text">Date: {formatDateForDisplay(parseDate(report.reportDate))
                    || <span className="text-muted">N/A</span>}
                  <span className="ml-1 float-right">
                      {report.syncError && <i className="fa fa-exclamation-circle sync-error-icon"/>}
                      <span className={"badge " + getStatusClassName(getDerivedStatusName(report))}>
                          {getDerivedStatusName(report)}
                      </span>
                  </span>
                </p>
            </div>
        </div>
    ));

    return (
        <Border title="PMC Reports" extraNavBar={
            <Link to="/report" className="btn btn-success"><i className="fa fa-plus"/></Link>
        }>
            <div className="container-fluid pb-3">

                <SyncErrorDialog />

                <TokenNoLongerValidDialog />

                {rows}

                {pmcReportArray.length === 0 ? <div className='text-muted text-center mt-3'>No reports.</div> : null}
            </div>
        </Border>
    );
}

function SyncErrorDialog() {
    const pmcReports = useSelector(selectPMCReports)
    const dispatch = useDispatch()

    const toggle = () => {
        dispatch(errorDialogClosed())
    }

    return (
        <Modal isOpen={pmcReports.showSyncError} toggle={toggle}>
            <ModalHeader toggle={toggle}>Error</ModalHeader>
            <ModalBody>
                <p>An error occurred while sending the reports to the server.</p>
                <p>Reports were reverted back to draft. Please check and submit again.</p>
            </ModalBody>
            <ModalFooter>
                <Button color="primary" onClick={toggle}>Ok</Button>
            </ModalFooter>
        </Modal>
    )
}

function TokenNoLongerValidDialog() {
    const pmcReports = useSelector(selectPMCReports)
    const dispatch = useDispatch()

    const toggle = () => {
        dispatch(tokenDialogClosed())
    }
    const goToLogin = () => {
        dispatch(performLogout())
    }

    return (
        <Modal isOpen={pmcReports.tokenIsExpired} toggle={toggle}>
            <ModalHeader toggle={toggle}>Session Expired</ModalHeader>
            <ModalBody>
                <p>You must login again to be able to submit new reports to the server.</p>
                <p>Pressing Ok will allow you continue to use the app in offline mode.</p>
                <p>Pressing Login redirect to login page and requires internet connection.</p>
            </ModalBody>
            <ModalFooter>
                    <Button color="primary" block onClick={toggle}>Ok</Button>
                <Button color="success" block onClick={goToLogin}>Login</Button>
            </ModalFooter>
        </Modal>
    )
}