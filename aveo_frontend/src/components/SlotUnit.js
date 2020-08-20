import {css, StyleSheet} from "aphrodite";
import React, {useState} from "react";
import {Container} from "react-bootstrap";

const styles = StyleSheet.create({
    container: {
        width: 180,
        margin: "10px 30px",
        borderRadius: 100,
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        border: "2px solid #007bff",
        ":hover": {
            background: "#8ec4ff",
            color: "white"
        }
    },
    label: {
        width: "100%",
        textAlign: "center",
        marginLeft: -13,
        display: 'block',
        position: 'relative',
        paddingLeft: '0',
        marginBottom: '0',
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
        height: '20px',
        width: '20px',
        backgroundColor: '#eee',

        ':after': {
            content: '',
            position: 'absolute',
            display: 'none',
        },
    },
})

const SlotUnit = (props) => {
    return (
        <div>
            <Container
                fluid='md'
                className={css(styles.container)}
                style={props.marked ? {
                    background: "#007bff",
                    color: "#ffffff"
                } : {background: "#ffffff"}}>
                <label className={css(styles.label)}>
                    <input style={{opacity: 0}} id={props.id} type="checkbox" readOnly
                           value={props.marked}
                           onClick={event => props.updateSlotState(event.target.id)}/>
                    {props.start_time}
                </label>
            </Container>
        </div>
    )
}

export default SlotUnit
