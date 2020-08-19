import React from 'react';
import {Calendar, Badge} from 'antd';
import 'antd/dist/antd.css';

import Editor from "./Editor"
import BulkEditor from "./BulkEditor"
import {Container, Button} from "react-bootstrap";
import {API, format} from "../URLs";
import {fetchMonthData, fetchSoltData, addSlots, delSlots, fetchValidSlots} from "../APIs"

class MonthView extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            month: "",
            month_view_data: "",
            editor: false,
            date: "",
            slotdata: null,
            slotdataFetched: false,
            slotStates: null,
            currentSlotStates: null,
            btnDisabled: true,

            bulkEditor: false,
            days: null,
            validSlots: null,
            markedCommonSLots: null
        }
        this.toggleEditor = this.toggleEditor.bind(this)
        this.componentDidMount = this.componentDidMount.bind(this)
        this.dateCellRender = this.dateCellRender.bind(this)
        this.getData = this.getData.bind(this)
        this.onPanelChange = this.onPanelChange.bind(this)
        this.onSelect = this.onSelect.bind(this)
    }

    componentDidMount() {
        var d = new Date();
        var m = d.getMonth() + 1;
        var y = d.getFullYear()
        fetchMonthData(this.props.tdata.id, m, y).then((data) => {
            this.setState({
                month: m,
                month_view_data: data
            })
        }).catch(error => console.log(error));
        this.mapDays()
        this.fetchAndParseValidSlots()
    }

    onPanelChange(value, mode) {
        const m = value.month() + 1
        const y = value.year()
        if (this.state.month !== m)
            fetchMonthData(this.props.tdata.id, m, y).then((data) => {
                this.setState({
                    month: m,
                    month_view_data: data
                })
            }).catch(error => console.log(error));
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
            content: counter + (counter === 1 ? " Slot" : " Marked") + " Available"
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

    toggleEditor() {
        const editorClosing = this.state.editor
        this.setState(prevState => {
                return {
                    editor: !prevState.editor,
                    slotdataFetched: !prevState.slotdataFetched
                }
            }
        )
        if (editorClosing) {
            var d = new Date(this.state.date)
            var m = d.getMonth() + 1
            var y = d.getFullYear()
            fetchMonthData(this.props.tdata.id, m, y).then((data) => {
                this.setState({
                    month: m,
                    month_view_data: data,
                })
            }).catch(error => console.log(error));
        }
    }

    fetchSlotsForDate = (date) => {
        return fetchSoltData(this.props.tdata.id, date)
            .then(
                data => {
                    this.setState({
                        slotdata: data,
                        date: date,
                        slotStates: this.parseAvailableSlot(data),
                        currentSlotStates: this.parseAvailableSlot(data),
                        btnDisabled: true
                    });
                }
            )
            .catch(error => console.log(error));
    }

    onSelect(value) {
        let date = value.format('YYYY-MM-DD')
        if (value.month() + 1 === this.state.month) {
            this.fetchSlotsForDate(date).then(() => this.toggleEditor())
        }
    }

    disabledDate(value) {
        var d = new Date()
        return value._d < d
    }

    fetchSoltData(tid, date) {
        var URL = API.BASE_URL + format(API.SLOT_GET_API_URL, [tid, date])

        return fetch(URL).then(response => response.json()).then(
            data => {
                this.setState({
                    slotdata: data,
                    date: date,
                    slotStates: this.parseAvailableSlot(data),
                    currentSlotStates: this.parseAvailableSlot(data),
                    btnDisabled: true
                });
            }
        )
    }

    parseAvailableSlot(slotData) {
        return slotData.map(slot => {
            return {
                id: slot.id,
                status: slot.slot.length > 0,
                available_slot_id: slot.slot.length > 0 ? slot.slot[0].id : null
            }
        })
    }

    updateSlotState = (id, status) => {
        this.setState(prevState => {
            const newSlotStatus = prevState.currentSlotStates.map(slot => {
                return {
                    id: slot.id,
                    status: slot.id.toString() === id ? status : slot.status,
                    available_slot_id: slot.available_slot_id
                }
            })
            return {
                currentSlotStates: newSlotStatus,
                btnDisabled: JSON.stringify(this.state.slotStates) === JSON.stringify(newSlotStatus)
            }
        })
    }

    APICall = (addSlotsList, delSlotsList) => {
        if (addSlotsList.length > 0 && delSlotsList.length > 0) {
            addSlots(JSON.stringify(addSlotsList)).then(response => {
                if (response.status === 201) {
                    console.log("Slots added")
                }
            }).then(() => {
                delSlots(this.props.tdata.id, JSON.stringify(delSlotsList))
                    .then(response => {
                        if (response.status === 204) {
                            console.log("Slots deleted")
                            this.fetchSlotsForDate(this.state.date).catch(error => console.log(error));
                        }
                    }).catch(error => console.log(error));
            }).catch(error => console.log(error));
        } else if (addSlotsList.length > 0) {
            addSlots(JSON.stringify(addSlotsList)).then(response => {
                if (response.status === 201) {
                    console.log("Slots added")
                }
            }).then(() => this.fetchSlotsForDate(this.state.date)).catch(error => console.log(error));
        } else if (delSlotsList.length > 0) {
            delSlots(this.props.tdata.id, JSON.stringify(delSlotsList)).then(response => {
                if (response.status === 204) {
                    console.log("Slots deleted")
                }
            }).then(() => this.fetchSlotsForDate(this.state.date)).catch(error => console.log(error));
        }
    }

    handleSave = () => {
        const changedSlots = this.state.currentSlotStates.filter(slot => {
            const id = slot.id
            return slot.status !== this.state.slotStates.filter(slot => {
                return slot.id === id
            })[0].status
        })
        const addSlots = changedSlots.filter(slot => slot.status).map(slot => {
            return {
                date: this.state.date,
                status: 1,
                teacher_id: this.props.tdata.id,
                validslot_id: slot.id
            }
        })
        const delSlots = changedSlots.filter(slot => !slot.status).map(slot => slot.available_slot_id)

        this.APICall(addSlots, delSlots)
    }

    // Bulk Editor
    mapDays = () => {
        let days = ["mon", "tue", "wed", "thu", "fri", "sat", "sun"]
        let mapping = []
        for (let i = 0; i < 7; i++) {
            mapping.push({
                day: days[i],
                marked: true
            })
        }
        this.setState(() => {
            return {days: mapping}
        })
    }

    toggleBulkEditor = () => {
        this.setState(prevState => {
            return {bulkEditor: !prevState.bulkEditor}
        })
    }

    updateDays = (weekDay) => {
        this.setState(prevState => {
            const updatedDays = prevState.days.map(day => {
                return {
                    day: day.day,
                    marked: weekDay === day.day ? !day.marked : day.marked
                }
            })
            const intersection = this.findCommonSlots(updatedDays)
            return {
                days: updatedDays,
                markedCommonSLots: intersection
            }
        })

    }

    updateBulkSlotState = () => {
        console.log("test")
    }

    fetchAndParseValidSlots = () => {
        fetchValidSlots().then(data => {
            let validSlotsMap = new Map()
            let days = ["mon", "tue", "wed", "thu", "fri", "sat", "sun"]
            for (let i = 0; i < 7; i++) validSlotsMap[days[i]] = {}
            data.map(slot => {
                validSlotsMap[days[slot.day]][slot.start_time] = slot.id
            })
            this.setState(() => {
                return {
                    validSlots: validSlotsMap
                }
            })
        }).then(() => {
            const intersection = this.findCommonSlots(this.state.days)
            this.setState(() => {
                return {
                    markedCommonSLots: intersection
                }
            })
        })
    }

    findCommonSlots = (markedDays) => {
        const validSlots = this.state.validSlots
        markedDays = markedDays.filter(day => day.marked)
        if (markedDays.length >0 ) {
            let intersection = new Set(Object.keys(validSlots[markedDays[0].day]))
            markedDays.forEach((day) => {
                let next = new Set(Object.keys(validSlots[day.day]))
                intersection = new Set([...intersection].filter(slot => next.has(slot)))
            })
            intersection = [...intersection].map(slot => {
                return {
                    start_time: slot,
                    marked: false
                }
            })
            return intersection
        }
        else return []
    }

    render() {
        return (
            <div>
                <Container fluid='md'>
                    <Calendar
                        dateCellRender={this.dateCellRender}
                        onPanelChange={this.onPanelChange}
                        onSelect={this.onSelect}
                        disabledDate={this.disabledDate}
                    />
                </Container>
                <Editor
                    show={this.state.editor}
                    date={this.state.date}
                    tdata={this.props.tdata}
                    slotdata={this.state.slotdata}
                    slotDataFetched={this.state.slotdataFetched}
                    updateSlotState={this.updateSlotState}
                    handleSave={this.handleSave}
                    handleClose={this.toggleEditor}
                    btnDisabled={this.state.btnDisabled}
                />
                {this.state.bulkEditor &&
                <BulkEditor
                    show={this.state.bulkEditor}
                    days={this.state.days}
                    updateDays={this.updateDays}
                    updateBulkSlotState={this.updateBulkSlotState}
                    commonSlots={this.state.markedCommonSLots}
                    handleClose={this.toggleBulkEditor}
                />
                }
                <Button variant="primary" onClick={this.toggleBulkEditor}>Bulk Availability</Button>
            </div>
        )
    }
}

export default MonthView
