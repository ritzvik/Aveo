// Example Usage of DateView component
// <DateView tdata={teacher object} date={"2020-08-20"} baseurl={"http://127.0.0.1:8000/"} />


import React, {useState} from 'react'
import {StyleSheet, css} from 'aphrodite'
import {Container} from "react-bootstrap"

const styles = StyleSheet.create({
    container: {
        width: 180,
        margin: "10px 30px",
        borderRadius: 100,
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        border: "2px solid #007bff",
        ":hover": {
            background: "#8ec4ff",
            color: "white"
        }
    },
    label: {
        width: "100%",
        textAlign: "center",
        marginLeft: -13,
        display: 'block',
        position: 'relative',
        paddingLeft: '0',
        marginBottom: '0',
        alignItems: 'center',
        justifyContent: 'center',
        cursor: 'pointer',
        fontSize: '22px',
        userSelect: 'none',

        ':input': {
            position: 'absolute',
            opacity: '0',
            cursor: 'pointer',
            height: '0',
            width: '0',
        },
    },
    checkmark: {
        position: 'absolute',
        top: '0',
        left: '0',
        height: '20px',
        width: '20px',
        backgroundColor: '#eee',

        ':after': {
            content: '',
            position: 'absolute',
            display: 'none',
        },
    },
})

class SlotUnit extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            marked: this.props.slot.slot.length > 0
        }
    }

    updateSlot = (event) => {
        this.setState(prevState => {
            return {marked: !prevState.marked}
        })
        this.props.updateSlotState(event.target.id, !this.state.marked)
    }

    render() {
        const styleSheet = StyleSheet.create({
            marked :{
                background: "#007bff"
            },
            unmarked :{
                background: "#ffffff"
            }
        })
        console.log(this.state.marked? styleSheet.marked :styleSheet.unmarked)
        return (
            <div>
                <Container fluid='md' className={css(styles.container)} style={this.state.marked? {background: "#007bff",color: "#ffffff"} :{background: "#ffffff"} } >
                    <label className={css(styles.label)}>
                        <input style={{opacity: 0}} id={this.props.id} type="checkbox" readOnly
                               checked={this.state.marked}
                               onClick={this.updateSlot}/>
                        {this.props.slot.start_time}
                    </label>
                </Container>
            </div>
        )
    }
}

class DateView extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            currentSlotStates: null
        }
    }

    dayList = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"];

    render() {
        var slots = null
        if (this.props.slotDataFetched)
            slots = this.props.slotData.map(slot => {
                return (
                    <SlotUnit
                        key={slot.id}
                        id={slot.id}
                        baseurl={this.props.baseurl}
                        slot={slot}
                        tid={this.props.tdata.id}
                        date={this.props.date}
                        thisDateView={this}
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
