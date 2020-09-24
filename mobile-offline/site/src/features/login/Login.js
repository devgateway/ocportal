import React, {useState, useEffect} from 'react';
import {useSelector, useDispatch} from 'react-redux';
import {useHistory} from "react-router-dom";
import {Spinner, Form, FormGroup, Label, Input, Button, Alert} from 'reactstrap';
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

    const history = useHistory();

    useEffect(() => {
        if (login.authenticated) {
            history.replace(login.user.changePasswordNextSignIn ? "/changePwd" : "/")
        }
    }, [login, history]);

    const handlePasswordRecovery = e => {
        e.preventDefault();
        history.push("/recoverPwd");
    }

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
                        <div>
                            <Button color="primary" block disabled={login.loading}>Submit</Button>{" "}
                        </div>
                        <div className="mt-3">
                            <Button color="primary" block disabled={login.loading} onClick={handlePasswordRecovery}>
                                Forgot your password?
                            </Button>
                        </div>
                    </Form>

                    {login.error &&
                        <Alert color="danger" className="mt-3">{login.error}</Alert>
                    }

                    {login.loading &&
                        <div className="d-flex justify-content-center">
                            <Spinner className="mt-3"/>
                        </div>
                    }
                </div>
            </div>
        </div>
    );
}
