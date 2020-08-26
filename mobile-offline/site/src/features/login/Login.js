import React, {useState} from 'react';
import {useSelector, useDispatch} from 'react-redux';
import {Spinner, Form, FormGroup, Label, Input, Button} from 'reactstrap';
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

    return (
        <div className="container-fluid mt-3">
            <div className="row login-form">
                <div className="col-10 offset-1 jumbotron">
                    <h2>Sign In</h2>
                    <Form onSubmit={submitForm}>
                        <FormGroup>
                            <Label>Username</Label>
                            <Input type="text" name="username" placeholder="username"
                                   value={userPass.username} readOnly={login.loading}
                                   onChange={updateField} required/>
                        </FormGroup>
                        <FormGroup>
                            <Label for="examplePassword">Password</Label>
                            <Input type="password" name="password" placeholder="********"
                                   value={userPass.password} readOnly={login.loading}
                                   onChange={updateField} required/>
                        </FormGroup>
                        <Button color="primary" disabled={login.loading}>Submit</Button>
                    </Form>
                    {login.loading ? <Spinner style={{width: '3rem', height: '3rem'}}/> : ""}
                </div>
            </div>
        </div>
    );
}
