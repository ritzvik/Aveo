import React from 'react'
import {Button, Modal} from "react-bootstrap";
import DateView from "./DateView";
import {API, format} from "../URLs"

class Editor extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            date: this.props.date,
            tdata: this.props.tdata,
            slotdata: null,
            slotdataFetched: false,
            slotStates: null,
            currentSlotStates: null,
            btnDisabled: true
        }
    }

    fetchSoltData(tid, date) {
        var URL = API.BASE_URL + format(API.SLOT_GET_API_URL, [tid, date])

        fetch(URL).then(response => response.json()).then(
            data => {
                this.setState({
                    slotdata: data,
                    slotdataFetched: true,
                    date: date,
                    slotStates: this.parseAvailableSlot(data),
                    currentSlotStates: this.parseAvailableSlot(data),
                    btnDisabled: true
                });
            }
        )
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (this.props.show && !this.state.slotdataFetched)
            this.fetchSoltData(this.state.tdata.id, this.props.date)
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
        // console.log(this.state.currentSlotStates)
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
        var URL = API.BASE_URL + API.SLOT_POST_API
        var requestOptions = {
            method: "POST",
            headers: {'Content-Type': 'application/json'},
            body: addSlotsList
        };
        fetch(URL, requestOptions).then(response => {
            if (response.status == 201) {
                var URL = API.BASE_URL + format(API.SLOT_DELETE_API, [this.state.tdata.id])
                var requestOptions = {
                    method: "DELETE",
                    headers: {'Content-Type': 'application/json'},
                    body: delSlotsList
                };
                fetch(URL, requestOptions).then(response => {
                    if (response.status === 204) {
                        this.fetchSoltData(this.state.tdata.id, this.state.date)
                    }
                }).then(
                    alert("Availability Updated.")
                )
            }
        })
    }

    handleSave = () => {
        const changedSlots = this.state.currentSlotStates.filter(slot => {
            const id = slot.id
            return slot.status !== this.state.slotStates.filter(slot => {
                return slot.id === id
            })[0].status
        })
        const addSlots = JSON.stringify(changedSlots.filter(slot => slot.status).map(slot => {
            return {
                date: this.state.date,
                status: 1,
                teacher_id: this.state.tdata.id,
                validslot_id: slot.id
            }
        }))
        const delSlots = JSON.stringify(changedSlots.filter(slot => !slot.status).map(slot => slot.available_slot_id))

        this.APICall(addSlots, delSlots)

    }

    onClose = () => {
        this.setState({slotdataFetched: false})
        this.props.handleClose()
    }

    render() {
        return (
            <div>
                <Modal show={this.props.show} onHide={this.onClose} centered size="xl"
                       dialogClassName="col-md-12">
                    <Modal.Header closeButton>
                        <Modal.Title>Select Availability</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        {this.state.slotdataFetched &&
                        <DateView
                            tdata={this.state.tdata}
                            date={this.state.date}
                            slotData={this.state.slotdata}
                            slotDataFetched={this.state.slotdataFetched}
                            updateSlotState={this.updateSlotState}
                            baseurl={API.BASE_URL}
                        />
                        }
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={this.onClose}>
                            Close
                        </Button>
                        <Button variant="primary"
                                onClick={this.handleSave}
                                disabled={this.state.btnDisabled}
                        >
                            Save Changes
                        </Button>
                    </Modal.Footer>
                </Modal>
            </div>
        );
    }
}

export default Editor
