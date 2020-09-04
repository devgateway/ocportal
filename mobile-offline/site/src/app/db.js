
export const loadUser = () => safeGetItem("user")

export const saveUser = user => safeSetItem("user", user)

export const deleteUser = () => {
    try {
        window.localStorage.removeItem("user")
    } catch (e) {
        // ignore
        console.log(e)
    }
}

export const loadReports = userId => safeGetItem(`reports-${userId}`)

export const saveReports = (userId, reports) => safeSetItem(`reports-${userId}`, reports)

export const loadMetadata = userId => safeGetItem(`metadata-${userId}`)

export const saveMetadata = (userId, metadata) => safeSetItem(`metadata-${userId}`, metadata)

const safeGetItem = key => {
    try {
        const str = window.localStorage.getItem(key)
        return str ? JSON.parse(str) : undefined
    } catch (e) {
        // ignore
        console.log(e)
        return undefined
    }
}

const safeSetItem = (key, value) => {
    try {
        window.localStorage.setItem(key, JSON.stringify(value));
    } catch (e) {
        // ignore
        console.log(e)
    }
}