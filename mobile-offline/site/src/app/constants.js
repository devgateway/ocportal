
export const PMCReportStatus = {
    DRAFT: "DRAFT",
    REJECTED: "REJECTED",
    SUBMITTED_PENDING: "SUBMITTED_PENDING",
    SUBMITTED: "SUBMITTED",
    APPROVED: "APPROVED",
    TERMINATED: "TERMINATED"
}

export const DATE_FORMAT = "yyyy/MM/dd";

export const isRejectedReport = report => report.status === PMCReportStatus.DRAFT && report.rejected
