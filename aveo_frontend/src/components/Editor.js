import React from 'react'
import {Button,Modal} from "react-bootstrap";
import DateView from "./DateView";

function Editor(props) {
    // console.log(props.data)
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
                        baseurl={"http://127.0.0.1:8000/"}
                    />
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => props.handleClose("")}>
                        Close
                    </Button>
                    <Button variant="primary">
                        Save Changes
                    </Button>
                </Modal.Footer>
            </Modal>
        </div>
    );
}

export  default Editor
