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

    getData(date, month) {
        let data = this.state.month_view_data;
        let item;
        let counter = 0
        for (item in data) {
            if (date === data[item].date) {
                console.log("True")
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

    componentDidMount() {
        var d = new Date();
        var m = d.getMonth()+1;
        var y = d.getFullYear()
        const URL = API.BASE_URL + format(API.MONTH_API_URL, [this.props.tdata.id, m, y])

        fetch(URL).then(response => response.json())
            .then((data) => {
                this.setState({month_view_data: data})
            });
    }

    onSelect(value) {
        console.log(value.format('YYYY-MM-DD'));
    }

    render() {
        return (
            <Container fluid='md'>
                <Calendar
                    dateCellRender={this.dateCellRender}
                    onSelect={this.onSelect}
                />
            </Container>
        )
    }
}

export default MonthView
