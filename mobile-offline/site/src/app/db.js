
import {PMCReportStatus} from "./constants";
import {hardcodedMetadata} from "./hardcoded";

// TODO use real database

const pmcReports = [{
    id: 1,
    fiscalYear: '2019/2020',
    department: {
        id: 10,
        label: 'Roads, Transport, Energy and Public Works'
    },
    tender: {
        id:342,
        tenderTitle: 'MAINTENANCE OF KWA TUVA-NGIINI-KALANZONI-MBONDONI-YAITHA ROAD'
    },
    date: '1995-11-17',
    subCounties: [{
        id: 1,
        label: 'Mbooni'
    }, {
        id: 2,
        label: 'Makueni'
    }],
    wards: [{
        id: 1,
        label: 'Kako/Waia'
    }],
    pmcMembers: [{
        id: 111,
        pmcStaff: {
            id: 10,
            label: 'Makua Peris',
            phone: '+254 735601847'
        },
        designation: {
            id: 32,
            label: 'Chairperson'
        }
    }],
    authorizePayment: true,
    notes: ["Note 1", "Note 2"],
    pmcStatus: {
        id: 10,
        name: 'At Risk'
    },
    projectClosureAndHandover: [{
        id: 2231,
        label: 'Wat?'
    }],
    eSignature: {
        fullName: 'Makua Peris',
        accepted: true
    },
    status: PMCReportStatus.DRAFT
}, {
    id: 2,
    fiscalYear: '2018/2019',
    department: {
        id: 11,
        label: 'Water, Sanitation, Environment and Climate Change'
    },
    tender: {
        id:342,
        tenderTitle: 'EXTENSION OF KIAONI PIPELINE EXTENSION'
    },
    date: '2020-05-20',
    subCounties: [{
        id: 2,
        label: 'Makueni'
    }],
    wards: [{
        id: 2,
        label: 'Mbitini'
    }],
    pmcMembers: [{
        id: 111,
        pmcStaff: {
            id: 10,
            label: 'Makua Peris',
            phone: '+254 735601847'
        },
        designation: {
            id: 32,
            label: 'Chairperson'
        }
    }],
    authorizePayment: false,
    notes: ["Note 1", "Note 2"],
    pmcStatus: {
        id: 11,
        name: 'On Track'
    },
    projectClosureAndHandover: [{
        id: 2232,
        label: 'Hospital Management committees'
    }],
    eSignature: {
        fullName: 'Makua Peris',
        accepted: true
    },
    status: PMCReportStatus.SUBMITTED
}];

const success = true;

export const loadPMCReports = () => {
    return new Promise((resolve, reject) => {
        setTimeout(() => {
            if (success) {
                resolve(pmcReports);
            } else {
                reject(new Error('Whoops!'));
            }
        }, 100);
    });
};

function generateGroupedMetadataItems(rawMetadata) {
    let metadata = {};

    for (const [key, value] of Object.entries(rawMetadata)) {
        metadata[key] = value;
        let byId = {};
        value.forEach(item => byId[item.id] = item);
        metadata[key+'ById'] = byId;
    }

    return metadata;
}

export const loadMetadata = () => {
    return new Promise((resolve, reject) => {
        console.log("Loading metadata from db...");
        setTimeout(() => {
            if (success) {
                resolve(generateGroupedMetadataItems(hardcodedMetadata));
            } else {
                reject(new Error('Whoops!'));
            }
        }, 100);
    });
};
