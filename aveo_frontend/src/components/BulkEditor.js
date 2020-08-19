import React from "react";
import {Button, Modal, Input, Form, Col} from "react-bootstrap"

const BulkEditor = props => {
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
    let checkBoxDays = null
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
    }
    console.log(props.days)
    return (
        <div>
            <Modal show={props.show} onHide={props.handleClose} centered size="xl"
                   dialogClassName="col-md-8">
                <Modal.Header closeButton>
                    <Modal.Title>Select Availability</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form style={StlyeSheet.Form}>
                        <Form.Row style={StlyeSheet.Row}>
                            <Col sm="2" style={StlyeSheet.Col}>
                                <Form.Label>Start Date: </Form.Label>
                            </Col>
                            <Col sm="3">
                                <Form.Control type="date"/>
                            </Col>
                        </Form.Row>
                        <Form.Row style={StlyeSheet.Row}>
                            <Col sm="2" style={StlyeSheet.Col}>
                                <Form.Label>End Date: </Form.Label>
                            </Col>
                            <Col sm="3">
                                <Form.Control type="date"/>
                            </Col>
                        </Form.Row>
                        <Form.Row>
                            <Col style={StlyeSheet.BtnCol}>
                            </Col>
                        </Form.Row>
                        <Form.Row>
                            {checkBoxDays}
                        </Form.Row>
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
