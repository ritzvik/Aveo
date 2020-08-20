import React from 'react';
import {Container, Form, Col, Button} from 'react-bootstrap';

class TeacherForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            teacher_id: ""
        }
        this.onChange = this.onChange.bind(this)
    }

    onChange(event) {
        this.setState({teacher_id: event.target.value})
    }

    render() {
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
                marginTop: 100
            }
        }
        return (
            <Container fluid='md' style={StlyeSheet.Container}>
                <Form style={StlyeSheet.Form}>
                    <Form.Row>
                        <Col md='12'>
                            <Form.Control placeholder="Teacher_ID" value={this.state.teacher_id}
                                          onChange={this.onChange}/>
                        </Col>
                    </Form.Row>
                    <Form.Row>
                        <Col style={StlyeSheet.BtnCol}>
                            <Button variant='primary'
                                    onClick={() => this.props.handleSubmit(this.state.teacher_id)}>Go</Button>
                        </Col>
                    </Form.Row>
                </Form>
            </Container>
        )
    }
}

export default TeacherForm
