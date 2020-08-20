// Example Usage of DateView component
// <DateView tdata={teacher object} date={"2020-08-20"} baseurl={"http://127.0.0.1:8000/"} />

import React from 'react'
import {Container} from "react-bootstrap"
import SlotUnit from "./SlotUnit"


class DateView extends React.Component {
    dayList = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"];

    render() {
        var slots = null
        if (this.props.slotDataFetched)
            slots = this.props.slotData.map(slot => {
                return (
                    <SlotUnit
                        key={slot.id}
                        id={slot.id}
                        marked={slot.slot.length > 0}
                        start_time={slot.start_time}
                        updateSlotState={this.props.updateSlotState}
                    />
                )
            })
        return (

            <div>
                {this.props.slotDataFetched &&
                <Container fluid='md'>
                    <h2>{this.props.date} | {this.dayList[this.props.slotData[0].day]}</h2>
                </Container>
                }
                {slots}
            </div>
        )
    }
}

export default DateView
