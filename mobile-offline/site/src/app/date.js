import moment from "moment";
import {API_DATE_FORMAT, DISPLAY_DATE_FORMAT} from "./constants";

export const parseDate = value => {
    return value
        ? moment(value, API_DATE_FORMAT).toDate()
        : undefined
}

export const formatDateForDisplay = date => {
    return date
        ? moment(date).format(DISPLAY_DATE_FORMAT)
        : undefined
}

export const formatDateForAPI = date => {
    return moment(date).format(API_DATE_FORMAT)
}
