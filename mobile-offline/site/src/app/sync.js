import {getMetadata, retrievePMCReports, updatePMCReport} from '../api/Api'
import {PMCReportStatus} from "./constants";

export const synchronize = async (userId, token, reports) => {

    const listReportsResponse = await retrievePMCReports(userId, token)

    const getMetadataResponse = await getMetadata(userId, token)

    let savedReports = {}

    if (reports.length > 0) {
        if (reports.some(report => report.status !== PMCReportStatus.SUBMITTED_PENDING)) {
            throw new Error(`Reports for sync must be in ${PMCReportStatus.SUBMITTED_PENDING} status`)
        }

        for (let i = 0; i < reports.length; i++) {
            let report = {
                ...reports[i],
                status: PMCReportStatus.SUBMITTED
            }
            try {
                let response = await updatePMCReport(userId, token, report)
                savedReports[report.internalId] = response.data
            } catch (e) {
                savedReports[report.internalId] = e.message
            }
        }
    }

    return {
        existingReports: listReportsResponse.data,
        savedReports: savedReports,
        metadata: getMetadataResponse.data
    }
}
