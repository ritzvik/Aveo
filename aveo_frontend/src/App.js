import React from "react";
import Header from "./components/Header";
import TeacherForm from "./components/TeacherForm";
import MonthView from "./components/MonthView";
import {API} from "./URLs"
import {getTeacher} from "./APIs"


class App extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            isLoggedIn: false,
            data: JSON.parse(localStorage.getItem(API.USER_KEY)) || ""
        }
        if (this.state.data !== "") this.state.isLoggedIn = true
        this.handleLogout = this.handleLogout.bind(this)
    }

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
                    ? <TeacherForm handleSubmit={this.handleBtnClick}/>
                    : <MonthView tdata={this.state.data}/>
                }
            </div>
        )
    }
}

export default App
