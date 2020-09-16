import {getMetadata, retrievePMCReports, updatePMCReports} from '../api/Api'
import {PMCReportStatus} from "./constants";

export const synchronize = async (userId, token, reports) => {

    const listReportsResponse = await retrievePMCReports(userId, token)

    const getMetadataResponse = await getMetadata(userId, token)

    let savedReports = []

    if (reports.length > 0) {
        try {
            if (reports.some(report => report.status !== PMCReportStatus.SUBMITTED_PENDING)) {
                throw new Error(`Reports for sync must be in ${PMCReportStatus.SUBMITTED_PENDING} status`)
            }

            const reportsWithCorrectStatus = reports.map(report => ({
                ...report,
                status: PMCReportStatus.SUBMITTED
            }))

            const savedReportsResponse = await updatePMCReports(userId, token, reportsWithCorrectStatus)

            savedReports = savedReportsResponse.data.map((report, idx) => ({
                ...report,
                internalId: reports[idx].internalId
            }))
        } catch (e) {
            console.log(e)

            savedReports = null
        }
    }

    return {
        existingReports: listReportsResponse.data,
        savedReports: savedReports,
        metadata: getMetadataResponse.data
    }
}
