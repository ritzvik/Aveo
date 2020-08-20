import {API, format} from "./URLs"

//Apis
//teacher data

const response = response => {
    if (response.status >= 200 && response.status < 300) {
        return Promise.resolve(response)
    } else if (response.status === 404) {
        return Promise.reject(new Error("URL not found :" + response.statusText))
    } else if (response.status >= 500 && response.status <= 504) {
        return Promise.reject(new Error("Server Error :" + response.statusText))
    } else {
        return Promise.reject(new Error(response.status))
    }
}

const fetchAPI = url => {
    return fetch(url).then(response).then(response => response.json())
}

export const getTeacher = id => {
    const URL = API.BASE_URL + format(API.TEACHER_API_URL, [id])
    return fetchAPI(URL)
}

//month view data
export const fetchMonthData = (tid, month, year) => {
    const URL = API.BASE_URL + format(API.MONTH_API_URL, [tid, month, year])
    return fetchAPI(URL)
}

//date view data
export const fetchSoltData = (tid, date) => {
    var URL = API.BASE_URL + format(API.SLOT_GET_API_URL, [tid, date])
    return fetchAPI(URL)
}

//add slots
export const addSlots = (addSlotsList) => {
    var URL = API.BASE_URL + API.SLOT_POST_API
    var requestOptions = {
        method: "POST",
        headers: {'Content-Type': 'application/json'},
        body: addSlotsList
    };
    return fetch(URL, requestOptions).then(response)
}

//delete slots
export const delSlots = (id, delSlotsList) => {
    var URL = API.BASE_URL + format(API.SLOT_DELETE_API, [id])
    var requestOptions = {
        method: "DELETE",
        headers: {'Content-Type': 'application/json'},
        body: delSlotsList
    };
    return fetch(URL, requestOptions).then(response)
}

// Get valid slots
export const fetchValidSlots = () => {
    const URL = API.BASE_URL + API.VALID_SLOTS_GET_API
    return fetchAPI(URL)
}
