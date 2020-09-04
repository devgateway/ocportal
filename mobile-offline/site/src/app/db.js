
import {hardcodedMetadata} from "./hardcoded";

const success = true;

export const loadUser = () => {
    try {
        const userStr = window.localStorage.getItem("user");
        return userStr ? JSON.parse(userStr) : undefined;
    } catch (e) {
        console.log(e)
        return undefined
    }
}

export const saveUser = user => {
    try {
        window.localStorage.setItem("user", JSON.stringify(user));
    } catch (e) {
        // ignore
        console.log(e)
    }
}

export const loadReports = userId => {
    try {
        const reportsStr = window.localStorage.getItem(`reports-${userId}`)
        return reportsStr ? JSON.parse(reportsStr) : undefined
    } catch (e) {
        console.log(e)
        return undefined
    }
};

export const saveReports = (userId, reports) => {
    try {
        window.localStorage.setItem(`reports-${userId}`, JSON.stringify(reports))
    } catch (e) {
        console.log(e)
    }
}

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
