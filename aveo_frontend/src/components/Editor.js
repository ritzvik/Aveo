import React from 'react'
import {Button, Modal} from "react-bootstrap";
import DateView from "./DateView";

const Editor = props => {
    return (
        <div>
            <Modal show={props.show} onHide={props.handleClose} centered size="xl"
                   dialogClassName="col-md-12">
                <Modal.Header closeButton>
                    <Modal.Title>Select Availability</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {props.slotDataFetched &&
                    <DateView
                        tdata={props.tdata}
                        date={props.date}
                        slotData={props.slotdata}
                        slotDataFetched={props.slotDataFetched}
                        updateSlotState={props.updateSlotState}
                    />
                    }
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={props.handleClose}>
                        Close
                    </Button>
                    <Button variant="primary"
                            onClick={props.handleSave}
                            disabled={props.btnDisabled}
                    >
                        Save Changes
                    </Button>
                </Modal.Footer>
            </Modal>
        </div>
    );
}

export default Editor
