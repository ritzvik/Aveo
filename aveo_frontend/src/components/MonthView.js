import React from 'react';
import {Calendar, Badge} from 'antd';
import 'antd/dist/antd.css';

import {Container} from "react-bootstrap";
import {API, format} from "../URLs";

class MonthView extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            month : "",
            month_view_data: ""
        }
        this.componentDidMount = this.componentDidMount.bind(this)
        this.dateCellRender = this.dateCellRender.bind(this)
        this.getData = this.getData.bind(this)
        this.fetchMonthData = this.fetchMonthData.bind(this)
        this.onPanelChange = this.onPanelChange.bind(this)
    }

    componentDidMount() {
        var d = new Date();
        var m = d.getMonth()+1;
        var y = d.getFullYear()
        this.fetchMonthData(m,y)
    }

    fetchMonthData(month, year){
        const URL = API.BASE_URL + format(API.MONTH_API_URL, [this.props.tdata.id, month, year])

        fetch(URL).then(response => response.json())
            .then((data) => {
                this.setState({
                    month: month,
                    month_view_data: data
                })
            });
    }

    onPanelChange(value, mode) {
        const m = value.month()+1
        const y = value.year()
        if (this.state.month !== m) this.fetchMonthData(m,y)
    }

    getData(date, month) {
        let data = this.state.month_view_data;
        let item;
        let counter = 0
        for (item in data) {
            if (date === data[item].date) {
                counter++;
            }
        }
        return month + 1 === this.state.month ? {
            valid_month: true,
            type: counter === 0 ? "warning" : "success",
            content: counter + (counter === 1 ? " Slot" : " Slots") + " Available"
        } : {valid_month: false}
    }

    dateCellRender(value) {

        var month = value.month()
        var month_data = this.getData(value.format('YYYY-MM-DD'), month)
        return (
            <div className="events">
                {month_data.valid_month &&
                <Badge status={month_data.type} text={month_data.content}/>
                }
            </div>
        );
    }

    onSelect(value) {
        // console.log(value.format('YYYY-MM-DD'));
    }

    render() {
        return (
            <Container fluid='md'>
                <Calendar
                    dateCellRender={this.dateCellRender}
                    onPanelChange={this.onPanelChange}
                    onSelect={this.onSelect}
                />
            </Container>
        )
    }
}

export default MonthView
