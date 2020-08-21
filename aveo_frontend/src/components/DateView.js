import React from 'react'
import {Container} from "react-bootstrap"
import SlotUnit from "./SlotUnit"

class DateView extends React.Component {
    dayList = [ "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];

    render() {
        var slots = null
        if (this.props.slotDataFetched)
            slots = this.props.slotData.map(slot => {
                return (
                    <SlotUnit
                        key={slot.id}
                        id={slot.id}
                        marked={slot.status}
                        start_time={slot.start_time}
                        updateSlotState={this.props.updateSlotState}
                    />
                )
            })
        return (

            <div>
                {this.props.slotDataFetched &&
                <Container fluid='md'>
                    <h2>{this.props.date} | {this.dayList[new Date(this.props.date).getDay()]}</h2>
                </Container>
                }
                {slots}
            </div>
        )
    }
}

export default DateView
