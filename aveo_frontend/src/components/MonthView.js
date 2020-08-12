import React, { useState } from 'react';

class MonthView extends React.Component {
    constructor(props) {
        super(props);
    }
    render() {
        return (
            <div> Welcome {this.props.tdata.first_name}!</div>
        )
    }
}

export default MonthView
