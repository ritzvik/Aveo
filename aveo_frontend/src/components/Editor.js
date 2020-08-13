import React from 'react'
import { Button, Modal } from "react-bootstrap";
import DateView from "./DateView";
import { API } from "../URLs"

function Editor(props) {

    return (
        <div>
            <Modal show={props.show} onHide={props.handleClose} centered size="xl" dialogClassName="col-md-12">
                <Modal.Header closeButton>
                    <Modal.Title>Select Availability</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <DateView
                        tdata={props.tdata}
                        date={props.date}
                        baseurl={API.BASE_URL}
                    />
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="primary" onClick={() => props.handleClose("")}>
                        Close
                    </Button>
                </Modal.Footer>
            </Modal>
        </div>
    );
}

export default Editor
