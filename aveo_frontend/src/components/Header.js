import React from "react"
import {Navbar, Nav} from 'react-bootstrap';

class Header extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <Navbar bg="light" expand="sm" className="bg-dark navbar-dark">
                <Navbar.Brand href="#home">TA Manager</Navbar.Brand>
                <Navbar.Toggle data-target="basic-navbar-nav"/>
                <Navbar.Collapse id="basic-navbar-nav">
                    {this.props.data.isLoggedIn &&
                    <Navbar.Text className="mr-auto">
                        Hi, {this.props.data.data.first_name} {this.props.data.data.last_name}
                    </Navbar.Text>
                    }
                    {this.props.data.isLoggedIn &&
                    <Nav>
                        <Nav.Link onClick={() => this.props.logout()}>Logout</Nav.Link>
                    </Nav>
                    }

                </Navbar.Collapse>
            </Navbar>
        )
    }
}

export default Header
