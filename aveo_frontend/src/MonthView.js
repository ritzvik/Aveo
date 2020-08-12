import React, { useState } from 'react';

export default function MonthView({ tid }) {

    const BASE_URL = "http://127.0.0.1:8000/"
    const [firstName, setFirstName] = useState(null)
    const [lastName, setLastName] = useState(null)

    function getTeacherDetails() {
        const TEACHER_API_URL = "ta2/api/teacher/"
        const URL = BASE_URL + TEACHER_API_URL + tid + "/"
        fetch(URL).then(response => response.json()).then(
            data => {
                setFirstName(data.first_name);
                setLastName(data.last_name);
            }
        )
    };

    if (tid) { getTeacherDetails(); }

    function fullName() {
        var fullname
        if (firstName == null || lastName == null) {
            fullname = "";
        }
        else {
            fullname = firstName + " " + lastName;
        }
        return fullname
    };

    return (<h1>Welcome {fullName()} !!!</h1>);
}
