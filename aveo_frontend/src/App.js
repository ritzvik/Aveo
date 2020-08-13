import React from "react";
import Header from "./components/Header";
import TeacherForm from "./components/TeacherForm";
import MonthView from "./components/MonthView";
import { API, format } from "./URLs"


class App extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            isLoggedIn: false,
            data: JSON.parse(localStorage.getItem(API.USER_KEY)) || ""
        }
        if (this.state.data !== "") this.state.isLoggedIn = true
        this.getTeacher = this.getTeacher.bind(this)
        this.handleLogout = this.handleLogout.bind(this)
    }

    getTeacher(id) {
        const URL = API.BASE_URL + format(API.TEACHER_API_URL, [id])

        fetch(URL).then(response => response.json())
            .then((data) => {
                localStorage.setItem(API.USER_KEY, JSON.stringify(data));
                this.setState({
                    isLoggedIn: true,
                    data: data
                })
            });
    }

    handleLogout() {
        localStorage.clear();
        this.setState({
            isLoggedIn: false,
            data: ""
        })
    }

    render() {
        return (
            <div>
                <Header
                    data={this.state}
                    logout={this.handleLogout}
                />
                {!this.state.isLoggedIn
                    ? <TeacherForm handleSubmit={this.getTeacher} />
                    : <MonthView tdata={this.state.data} />
                }
            </div>
        )
    }
}

export default App
