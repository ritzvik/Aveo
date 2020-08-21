import React from "react";
import { Spinner } from "react-bootstrap";

const Loader = (props) =>  {
    const display = props.show? "flex": "none"
    const style = {
        Container: {
            position: "fixed",
            top: 0,
            width: "100%",
            height: "100%",
            display: display,
            justifyContent: "center",
            alignItems: "center",
            background: "rgba(0,10,45,0.33)",
            zIndex: 10000,
            overflow: "hidden"
    }}
    return (
        <div style={style.Container}>
            <Spinner animation="border" />
        </div>
    )
}

export default Loader
