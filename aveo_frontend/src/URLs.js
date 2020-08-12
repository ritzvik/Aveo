// APIs URLS
export const API = {
    USER_KEY : 'id',
    BASE_URL : "http://127.0.0.1:8000/",
    TEACHER_API_URL : "ta2/api/teacher/{0}/",
    MONTH_API_URL: "ta2/api/availableslot/tid/{0}/m/{1}/y/{2}/"
}

export const format = (string, arg) =>  {
    var formatted = string;
    for (var i = 0; i < string.length; i++) {
        var regexp = new RegExp('\\{'+i+'\\}', 'gi');
        formatted = formatted.replace(regexp, arg[i]);
    }
    return formatted;
}
