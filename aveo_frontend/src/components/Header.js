import React from "react"
import {Navbar, Nav} from 'react-bootstrap';

const Header = props => {
        return (
            <Navbar bg="light" expand="sm" className="bg-dark navbar-dark">
                <Navbar.Brand href="#home">TA Manager</Navbar.Brand>
                <Navbar.Toggle data-target="basic-navbar-nav"/>
                <Navbar.Collapse id="basic-navbar-nav">
                    {props.data.isLoggedIn &&
                    <Navbar.Text className="mr-auto">
                        Hi, {props.data.data.first_name} {props.data.data.last_name}
                    </Navbar.Text>
                    }
                    {props.data.isLoggedIn &&
                    <Nav>
                        <Nav.Link onClick={() => props.logout()}>Logout</Nav.Link>
                    </Nav>
                    }

                </Navbar.Collapse>
            </Navbar>
        )
}

export default Header
