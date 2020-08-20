import React from 'react';
import {Calendar, Badge, Select, Col, Typography, Radio, Row} from 'antd';
import 'antd/dist/antd.css';

import Editor from "./Editor"
import BulkEditor from "./BulkEditor"
import {Container, Button} from "react-bootstrap";
import {fetchMonthData, fetchSoltData, addSlots, delSlots, fetchValidSlots} from "../APIs"

class MonthView extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            month: "",
            month_view_data: "",
            year: "",
            editor: false,
            date: "",
            slotdata: null,
            slotdataFetched: false,
            slotStates: null,
            currentSlotStates: null,
            btnDisabled: true,

            bulkEditor: false,
            minDate: null,
            maxDate: null,
            days: null,
            validSlots: null,
            markedCommonSLots: null,
            bulkBtnDisabled: false
        }
        this.componentDidMount = this.componentDidMount.bind(this)
    }

    getDateString = (d, m, y) => {
        return y + '-' + (m <= 9 ? '0' + m : m) + '-' + (d <= 9 ? '0' + d : d);
    }

    componentDidMount() {
        let d = new Date();
        let dd = d.getDate()
        let m = d.getMonth() + 1;
        let y = d.getFullYear()
        let minDate = this.getDateString(dd, m, y)
        d = new Date(y, m, 0).getDate()
        let maxDate = this.getDateString(d, m, y)
        fetchMonthData(this.props.tdata.id, m, y).then((data) => {
            this.setState({
                month: m,
                year: y,
                month_view_data: data,
                minDate: minDate,
                maxDate: maxDate
            })
        }).catch(error => console.log(error));
        this.mapDays()
        this.fetchAndParseValidSlots()
    }

    onPanelChange = (value, mode) => {
        const m = value.month() + 1
        const y = value.year()
        let minDate = null
        let maxDate = null
        let btnDisable = false
        if (m > new Date().getMonth() + 1) {
            minDate = this.getDateString(1, m, y)
            let d = new Date(y, m, 0).getDate()
            maxDate = this.getDateString(d, m, y)
        } else if (m === new Date().getMonth() + 1) {
            minDate = this.getDateString(new Date().getDate(), m, y)
            let d = new Date(y, m, 0).getDate()
            maxDate = this.getDateString(d, m, y)
        } else btnDisable = true
        if (this.state.month !== m || this.state.year !== y)
            fetchMonthData(this.props.tdata.id, m, y).then((data) => {
                this.setState({
                    month: m,
                    year: y,
                    month_view_data: data,
                    minDate: minDate,
                    maxDate: maxDate,
                    bulkBtnDisabled: btnDisable
                })
            }).catch(error => console.log(error));
    }

    getData = (date, month) => {
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

    dateCellRender = (value) => {
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

    toggleEditor = () => {
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
                    year: y,
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

    onSelect = (value) => {
        let date = value.format('YYYY-MM-DD')
        if (value.month() + 1 === this.state.month && value.year() === this.state.year) {
            this.fetchSlotsForDate(date).then(() => this.toggleEditor())
        }
    }

    disabledDate = (value) => {
        var d = new Date()
        return value._d < d
    }

    parseAvailableSlot = (slotData) => {
        return slotData.map(slot => {
            return {
                id: slot.id,
                status: slot.slot.length > 0,
                available_slot_id: slot.slot.length > 0 ? slot.slot[0].id : null,
                start_time: slot.start_time
            }
        })
    }

    updateSlotState = (id) => {
        this.setState(prevState => {
            const newSlotStatus = prevState.currentSlotStates.map(slot => {
                return {
                    id: slot.id,
                    status: slot.id.toString() === id ? !slot.status : slot.status,
                    available_slot_id: slot.available_slot_id,
                    start_time: slot.start_time
                }
            })
            return {
                currentSlotStates: newSlotStatus,
                btnDisabled: JSON.stringify(this.state.slotStates) === JSON.stringify(newSlotStatus)
            }
        })
    }

    updateSlotAvailability = (addSlotsList, delSlotsList) => {
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

        this.updateSlotAvailability(addSlots, delSlots)
    }

    // --------------Bulk Editor-------------
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

    updateBulkSlotState = (id) => {
        this.setState(prevState => {
            let commonSlots = prevState.markedCommonSLots
            commonSlots = commonSlots.map(slot => {
                return {
                    start_time: slot.start_time,
                    marked: slot.start_time === id ? !slot.marked : slot.marked
                }
            })
            return {markedCommonSLots: commonSlots}
        })
    }

    fetchAndParseValidSlots = () => {
        fetchValidSlots().then(data => {
            let validSlotsMap = new Map()
            let days = ["mon", "tue", "wed", "thu", "fri", "sat", "sun"]
            for (let i = 0; i < 7; i++) validSlotsMap[days[i]] = {}
            data.forEach(slot => {
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
        if (markedDays.length > 0) {
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
        } else return []
    }

    handleBulkSave = (startDate, endDate) => {
        startDate = new Date(startDate)
        endDate = new Date(endDate)
        let date = new Date(startDate)
        let days = ["sun", "mon", "tue", "wed", "thu", "fri", "sat"]
        const markedSlots = this.state.markedCommonSLots
        const markedDays = this.state.days.filter(day => day.marked)
        const validSlots = this.state.validSlots

        let monthDataMap = new Map()
        this.state.month_view_data.forEach(slot => {
            const mapID = slot.date + "/" + slot.validslot_id
            monthDataMap.set(mapID, true)
        })
        let addSlotsList = []
        while (date <= endDate) {
            const d = date.getDate()
            const day = days[date.getDay()]
            const m = date.getMonth() + 1
            const y = date.getFullYear()
            const dateString = this.getDateString(d, m, y)
            const validDay = markedDays.filter(dayItem => dayItem.day === day)

            if (validDay.length > 0) {
                addSlotsList = [...addSlotsList, ...markedSlots.filter(slot => {
                    const validSlotsID = validSlots[day][slot.start_time]
                    return slot.marked && !monthDataMap.has(dateString + "/" + validSlotsID)
                }).map(slot => {
                    const validSlotsID = validSlots[day][slot.start_time]
                    return {
                        date: dateString,
                        status: 1,
                        teacher_id: this.props.tdata.id,
                        validslot_id: validSlotsID
                    }
                })]
            }
            date = new Date(date.setDate(date.getDate() + 1))
        }
        if (addSlotsList.length > 0) {
            addSlots(JSON.stringify(addSlotsList)).then(response => {
                if (response.status === 201) {
                    console.log("Slots added")
                }
            }).then(() => {
                fetchMonthData(this.props.tdata.id, this.state.month, this.state.year).then((data) => {
                    this.setState(prevState => {
                        return {
                            month_view_data: data,
                            bulkEditor: !prevState.bulkEditor
                        }
                    })
                })
            }).catch(error => console.log(error));
        }
    }

    headerRender = ({value, type, onChange, onTypeChange}) => {
        const start = 0;
        const end = 12;
        const monthOptions = [];

        const current = value.clone();
        const localeData = value.localeData();
        const months = [];
        for (let i = 0; i < 12; i++) {
            current.month(i);
            months.push(localeData.monthsShort(current));
        }

        for (let index = start; index < end; index++) {
            monthOptions.push(
                <Select.Option className="month-item" key={`${index}`}>
                    {months[index]}
                </Select.Option>,
            );
        }
        const month = value.month();

        const year = value.year();
        const options = [];
        for (let i = year - 10; i < year + 10; i += 1) {
            options.push(
                <Select.Option key={i} value={i} className="year-item">
                    {i}
                </Select.Option>,
            );
        }

        return (
            <div style={{padding: 8}}>
                <Typography.Title level={2} style={{color: "#555555"}}>
                    Manage Availability
                </Typography.Title>
                <Row gutter={8}>
                    <Col>
                        <Radio.Group size="small" onChange={e => onTypeChange(e.target.value)} value={type}>
                            <Radio.Button value="month">Month</Radio.Button>
                        </Radio.Group>
                    </Col>
                    <Col>
                        <Select
                            size="small"
                            dropdownMatchSelectWidth={false}
                            className="my-year-select"
                            onChange={newYear => {
                                const now = value.clone().year(newYear);
                                onChange(now);
                            }}
                            value={String(year)}
                        >
                            {options}
                        </Select>
                    </Col>
                    <Col>
                        <Select
                            size="small"
                            dropdownMatchSelectWidth={false}
                            value={String(month)}
                            onChange={selectedMonth => {
                                const newValue = value.clone();
                                newValue.month(parseInt(selectedMonth, 10));
                                onChange(newValue);
                            }}
                        >
                            {monthOptions}
                        </Select>
                    </Col>
                </Row>
            </div>
        );
    }

    render() {
        return (
            <div>
                <Container fluid='md'>
                    <Calendar
                        headerRender={this.headerRender}
                        dateCellRender={this.dateCellRender}
                        onPanelChange={this.onPanelChange}
                        onSelect={this.onSelect}
                        disabledDate={this.disabledDate}
                    />
                    <Button
                        style={{margin: 5}}
                        variant="primary"
                        onClick={this.toggleBulkEditor}
                        disabled={this.state.bulkBtnDisabled}
                    >Add Availabilities for Month</Button>
                </Container>
                <Editor
                    show={this.state.editor}
                    date={this.state.date}
                    tdata={this.props.tdata}
                    slotdata={this.state.currentSlotStates}
                    slotDataFetched={this.state.slotdataFetched}
                    updateSlotState={this.updateSlotState}
                    handleSave={this.handleSave}
                    handleClose={this.toggleEditor}
                    btnDisabled={this.state.btnDisabled}
                />
                {this.state.bulkEditor &&
                <BulkEditor
                    show={this.state.bulkEditor}
                    minDate={this.state.minDate}
                    maxDate={this.state.maxDate}
                    days={this.state.days}
                    updateDays={this.updateDays}
                    updateBulkSlotState={this.updateBulkSlotState}
                    commonSlots={this.state.markedCommonSLots}
                    handleClose={this.toggleBulkEditor}
                    handleSave={this.handleBulkSave}
                />
                }
            </div>
        )
    }
}

export default MonthView
