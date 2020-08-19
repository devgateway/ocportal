import React, {useState} from 'react';
import {useSelector, useDispatch} from 'react-redux';
import {Form, FormGroup, Label, Input, Button} from 'reactstrap';
import {performLogin, selectLogin} from './loginSlice';

export function Login() {
    const login = useSelector(selectLogin);
    const dispatch = useDispatch();
    const [userPass, setUserPass] = useState({username: '', password: ''});

    const submitForm = (event) => {
        event.preventDefault();
        dispatch(performLogin(userPass));
    };

    const updateField = e => {
        setUserPass({
            ...userPass,
            [e.target.name]: e.target.value
        });
    };

    if (login.authenticated === true) {
        return (
            <div className="row">
                <div className="col-8 offset-2">
                    <h2>User {login.username} authenticated</h2>
                </div>
            </div>
        );
    } else {
        return (
            <div>
                <div className="row login-form">
                    <div className="col-4 offset-4 jumbotron">
                        <h2>Sign In</h2>
                        <Form onSubmit={submitForm}>
                            <FormGroup>
                                <Label>Username</Label>
                                <Input type="text" name="username" placeholder="username"
                                       value={userPass.username}
                                       onChange={updateField} required/>
                            </FormGroup>
                            <FormGroup>
                                <Label for="examplePassword">Password</Label>
                                <Input type="password" name="password" placeholder="********"
                                       value={userPass.password}
                                       onChange={updateField} required/>
                            </FormGroup>
                            <Button color="primary">Submit</Button>
                        </Form>
                    </div>
                </div>
            </div>
        );
    }
}