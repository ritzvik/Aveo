import React from "react";
import Header from "./components/Header";
import TeacherForm from "./components/TeacherForm";
import MonthView from "./components/MonthView";

// import Data from './data/Data'

const USER_KEY = 'id'

class App extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            isLoggedIn: false,
            data: JSON.parse(localStorage.getItem(USER_KEY)) || ""
        }
        if (this.state.data !== "") this.state.isLoggedIn = true
        this.getTeacher = this.getTeacher.bind(this)
        this.handleLogout = this.handleLogout.bind(this)
    }


    BASE_URL = "http://127.0.0.1:8000/"

    getTeacher(id) {
        const TEACHER_API_URL = "ta2/api/teacher/"
        const URL = this.BASE_URL + TEACHER_API_URL + id + "/"

        fetch(URL).then(response => response.json())
            .then((data) => {
                localStorage.setItem(USER_KEY, JSON.stringify(data));
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
                    ? <TeacherForm handleSubmit={this.getTeacher}/>
                    : <MonthView tdata={this.state.data}/>
                }
            </div>
        )
    }
}

export default App
