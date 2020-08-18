import React from "react";
import Header from "./components/Header";
import TeacherForm from "./components/TeacherForm";
import MonthView from "./components/MonthView";
import { API, format } from "./URLs"
import {getTeacher} from "./APIs"


class App extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            isLoggedIn: false,
            data: JSON.parse(localStorage.getItem(API.USER_KEY)) || ""
        }
        if (this.state.data !== "") this.state.isLoggedIn = true
        // this.getTeacher = this.getTeacher.bind(this)
        this.handleLogout = this.handleLogout.bind(this)
    }

    // getTeacher(id) {
    //     const URL = API.BASE_URL + format(API.TEACHER_API_URL, [id])
    //     const res = response => {
    //         if (response.status >= 200 && response.status < 300) {
    //             return Promise.resolve(response)
    //         } else if (response.status === 404 ) {
    //             return Promise.reject(new Error("Teacher not found "+response.statusText))
    //         } else {
    //             return Promise.reject(new Error(response.status))
    //         }
    //     }
    //     fetch(URL).then(res).then(response=> response.json())
    //         .then((data) => {
    //             // console.log(data.code)
    //             localStorage.setItem(API.USER_KEY, JSON.stringify(data));
    //             this.setState({
    //                 isLoggedIn: true,
    //                 data: data
    //             })
    //         }).catch(error => console.log(error));
    // }

    handleLogout() {
        localStorage.clear();
        this.setState({
            isLoggedIn: false,
            data: ""
        })
    }
    handleBtnClick = (id) => {
        getTeacher(id).then((data) => {
            localStorage.setItem(API.USER_KEY, JSON.stringify(data));
            this.setState({
                isLoggedIn: true,
                data: data
            })
        }).catch(error => console.log(error));
    }

    render() {
        return (
            <div>
                <Header
                    data={this.state}
                    logout={this.handleLogout}
                />
                {!this.state.isLoggedIn
                    ? <TeacherForm handleSubmit={this.handleBtnClick} />
                    : <MonthView tdata={this.state.data} />
                }
            </div>
        )
    }
}

export default App
