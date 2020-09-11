
export const PMCReportStatus = {
    DRAFT: "DRAFT",
    REJECTED: "REJECTED",
    SUBMITTED_PENDING: "SUBMITTED_PENDING",
    SUBMITTED: "SUBMITTED",
    APPROVED: "APPROVED",
    TERMINATED: "TERMINATED"
}

// date format for display (momentjs)
export const DISPLAY_DATE_FORMAT = "DD/MM/YYYY";

// date format for display (date picker component)
export const DP_DATE_FORMAT = "dd/MM/yyyy";

// API date format (momentjs)
export const API_DATE_FORMAT = "YYYY-MM-DD";

export const isRejectedReport = report => report.status === PMCReportStatus.DRAFT && report.rejected
