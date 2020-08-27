
import {hardcodedMetadata} from "./hardcoded";

// TODO use real database

const pmcReports = [
    {
        "id": 65624,
        "tenderId": 30338,
        "authorizePayment": false,
        "subcountyIds": [
            15852,
            15853
        ],
        "wardIds": [
            15858
        ],
        "pmcMembers": [
            {
                "id": 65628,
                "staffId": 65620,
                "designationId": 65622
            },
            {
                "id": 78869
            }
        ],
        "pmcNotes": [
            {
                "id": 79088,
                "text": "a note"
            },
            {
                "id": 79089,
                "text": "another note"
            }
        ],
        "statusComments": [
            {
                "id": 65625,
                "status": "SUBMITTED",
                "createdBy": "admin",
                "createdDate": "2020-05-26T09:59:34.498+03:00"
            },
            {
                "id": 65630,
                "status": "APPROVED",
                "createdBy": "admin",
                "createdDate": "2020-05-26T10:00:45.668+03:00"
            },
            {
                "id": 69082,
                "status": "DRAFT",
                "createdBy": "admin",
                "createdDate": "2020-05-26T21:04:37.031+03:00"
            },
            {
                "id": 78734,
                "status": "DRAFT",
                "comment": "this is a comment",
                "createdBy": "admin",
                "createdDate": "2020-08-04T17:33:48.009+03:00"
            }
        ],
        "pmcStatusId": 54724,
        "projectClosureHandoverIds": [
            54735
        ],
        "signatureNames": "MY NAMES",
        "status": "DRAFT",
        "reportDate": "2020-05-25T21:00:00Z"
    },
    {
        "id": 69843,
        "tenderId": 69649,
        "authorizePayment": true,
        "subcountyIds": [
            15853
        ],
        "wardIds": [
            15859
        ],
        "pmcMembers": [
            {
                "id": 69847,
                "staffId": 65620,
                "designationId": 65622
            }
        ],
        "statusComments": [
            {
                "id": 69844,
                "status": "SUBMITTED",
                "createdBy": "admin",
                "createdDate": "2020-05-29T07:25:55.569+03:00"
            },
            {
                "id": 74815,
                "status": "APPROVED",
                "createdBy": "admin",
                "createdDate": "2020-06-29T20:15:51.351+03:00"
            }
        ],
        "pmcStatusId": 54725,
        "projectClosureHandoverIds": [
            54734
        ],
        "status": "APPROVED",
        "reportDate": "2010-01-31T21:00:00Z"
    }
];

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
