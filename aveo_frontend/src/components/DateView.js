// Example Usage of DateView component
// <DateView tdata={teacher object} date={"2020-08-20"} baseurl={"http://127.0.0.1:8000/"} />


import React, { useState } from 'react'
import { StyleSheet, css } from 'aphrodite'

const styles = StyleSheet.create({
    container: {
        display: 'block',
        position: 'relative',
        paddingLeft: '35px',
        marginBottom: '12px',
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
        height: '25px',
        width: '25px',
        backgroundColor: '#eee',

        ':after': {
            content: '',
            position: 'absolute',
            display: 'none',
        },
    },
})

function SlotUnit({ baseurl, slot, tid, date, thisDateView }) {
    var markedAvailable = false
    if (slot.slot.length > 0) {
        markedAvailable = true
    }

    function falsifySlotDataFetched() {
        thisDateView.setState({
            slotdataFetched: false,
        })
    };

    function toggleAvailability() {
        var API_URL = "ta2/api/availableslot/"
        if (markedAvailable) {
            var API_URL_DELETE = API_URL + slot.slot[0].id + "/"
            var URL = baseurl + API_URL_DELETE
            var requestOptions = {
                method: "DELETE",
                headers: { 'Content-Type': 'application/json' },
            };

            fetch(URL, requestOptions).then(() => {
                falsifySlotDataFetched();
            })
        }
        else {
            var requestOptions = {
                method: "POST",
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    date: date,
                    status: 1,
                    teacher_id: tid,
                    validslot_id: slot.id
                })
            };
            var URL = baseurl + API_URL

            fetch(URL, requestOptions).then(response => response.json())
                .then(data => {
                    console.log(data);
                    falsifySlotDataFetched();
                })
        }
    };

    return (
        <>
            <label class={css(styles.container)}>
                <input type="checkbox" readOnly checked={markedAvailable} onClick={toggleAvailability} />
                <span class={css(styles.checkmark)} ></span>
                {slot.start_time}
            </label>
        </>
    )
}

class DateView extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            date: this.props.date,
            tdata: this.props.tdata,
            baseurl: this.props.baseurl,
            slotdata: null,
            slotdataFetched: false,
        }
    }
    dayList = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"];


    fetchSoltData(tid, date) {
        var API_URL = "ta2/api/availableslot/tid/" + tid + "/date/" + date + "/"
        var URL = this.state.baseurl + API_URL

        fetch(URL).then(response => response.json()).then(
            data => {
                console.log(data);
                this.setState({
                    slotdata: data,
                    slotdataFetched: true,
                });
            }
        )
    }

    render() {
        if (!this.state.slotdataFetched) {
            this.fetchSoltData(this.state.tdata.id, this.state.date);
        }
        if (this.state.slotdataFetched) {
            return (
                <>
                    <h2>{this.state.date} | {this.dayList[this.state.slotdata[0].day]}</h2>
                    {
                        this.state.slotdata.map(slot => {
                            return (
                                <SlotUnit
                                    key={slot.id}
                                    baseurl={this.state.baseurl}
                                    slot={slot}
                                    tid={this.state.tdata.id}
                                    date={this.state.date}
                                    thisDateView={this}
                                />
                            )
                        })
                    }
                </>
            )
        }
        return null
    }
}

export default DateView
