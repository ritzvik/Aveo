import React, { useState } from "react";
import {Button, Modal, Input, Form, Col} from "react-bootstrap"
import SlotUnit from "./SlotUnit";

const BulkEditor = props => {
    const [startDate, setStartDate] = useState(props.minDate)
    const [endDate, setEndtDate] = useState(props.maxDate)
    const StlyeSheet = {
        Container: {
            display: 'flex',
            justifyContent: 'center'
        },
        BtnCol: {
            margin: 20,
            display: 'flex',
            justifyContent: 'center'
        },
        Form: {
            marginTop: 15
        },
        Row: {
            margin: 5
        },
        Col: {
            alignItems: "center",
            display: "flex"
        }
    }

    const month = [ "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December" ]

    let checkBoxDays = null
    let slots = null
    if (props.show) {
        checkBoxDays = props.days.map(day => {
            return <label key={day.day} style={{padding: "5px 20px"}}>
                <input id={day.day} type="checkbox" readOnly
                       checked={day.marked}
                       onClick={(event) => props.updateDays(event.target.id)}
                />
                <span style={{padding: 5}}>{day.day.charAt(0).toUpperCase() + day.day.slice(1)}</span>
            </label>
        })
        slots = props.commonSlots.map(slot => {
            // console.log(slot.marked)
            return <SlotUnit
                key={slot.start_time}
                id={slot.start_time}
                state={false}
                marked={slot.marked}
                start_time={slot.start_time}
                updateSlotState={props.updateBulkSlotState}
            />
        })
    }

    return (
        <div>
            <Modal show={props.show} onHide={props.handleClose} centered size="xl"
                   dialogClassName="col-md-8">
                <Modal.Header closeButton>
                    <Modal.Title>Select Availability |
                         {" "+month[new Date(props.minDate).getMonth()]} {new Date(props.minDate).getFullYear()}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form style={StlyeSheet.Form}>
                        <Form.Row style={StlyeSheet.Row}>
                            <Col sm="2" style={StlyeSheet.Col}><Form.Label>Start Date: </Form.Label></Col>
                            <Col sm="3">
                                <Form.Control
                                    value={startDate}
                                    type="date"
                                    min={props.minDate}
                                    max={endDate}
                                    onChange={e => {
                                        setStartDate(e.target.value)
                                    }}
                                /></Col>
                        </Form.Row>
                        <Form.Row style={StlyeSheet.Row}>
                            <Col sm="2" style={StlyeSheet.Col}><Form.Label>End Date: </Form.Label></Col>
                            <Col sm="3">
                                <Form.Control
                                    type="date"
                                    min={startDate}
                                    max={props.maxDate}
                                    onChange={e => setEndtDate(e.target.value)}/></Col>
                        </Form.Row>
                        <Form.Row><Col>{checkBoxDays}</Col></Form.Row>
                        <Form.Row>
                            <Col><Form.Label style={{fontSize: 20, padding: "0 5px"}}>Select Slots: </Form.Label></Col>
                        </Form.Row>
                        {slots}
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={props.handleClose}>
                        Close
                    </Button>
                    <Button variant="primary">
                        Save Changes
                    </Button>
                </Modal.Footer>
            </Modal>
        </div>
    )
}

export default BulkEditor
