import React from 'react';
import {Calendar, Badge} from 'antd';
import 'antd/dist/antd.css';

import {Container} from "react-bootstrap";
import {API, format} from "../URLs";

class MonthView extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            month_view_data: ""
        }
        this.componentDidMount = this.componentDidMount.bind(this)
        this.dateCellRender = this.dateCellRender.bind(this)
        this.getData = this.getData.bind(this)
    }

    getData(date, month, year) {
        // console.log(date, month, year);
        let data = this.state.month_view_data;
        let item;
        let counter = 0
        for (item in data) {
            var bool_date = date === parseInt(data[item].date.split("-")[2])
            var bool_month = month + 1 === parseInt(data[item].date.split("-")[1])
            var bool_year = year === parseInt(data[item].date.split("-")[0])
            if (bool_date && bool_month && bool_year) {
                counter++;
            }
        }
        return month + 1 === 8 ? {
            valid_month: true,
            type: counter === 0 ? "warning" : "success",
            content: counter + (counter === 1 ? " Slot" : " Slots") + " Available"
        } : {valid_month: false}
    }

    dateCellRender(value) {
        var date = value.date()
        var month = value.month()
        var year = value.year()
        var month_data = this.getData(date, month, year)
        return (
            <div className="events">
                {month_data.valid_month &&
                <Badge status={month_data.type} text={month_data.content}/>
                }
            </div>
        );
    }

    componentDidMount() {
        const URL = API.BASE_URL + format(API.MONTH_API_URL, [2, 8, 2020])

        fetch(URL).then(response => response.json())
            .then((data) => {
                console.log(data)
                // localStorage.setItem(API.USER_KEY, JSON.stringify(data));
                this.setState({month_view_data: data})
            });
    }

    onPanelChange(value, mode) {
        console.log(value.format('YYYY-MM-DD'), mode);
    }

    render() {
        return (
            <Container fluid='md'>
                {/*Welcome {this.props.tdata.first_name}!*/}
                <Calendar
                    dateCellRender={this.dateCellRender}
                    onPanelChange={this.onPanelChange}
                />
            </Container>
        )
    }
}

export default MonthView
