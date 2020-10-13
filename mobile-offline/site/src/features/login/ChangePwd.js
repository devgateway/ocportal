import {Alert, Button, Form, FormFeedback, FormGroup, Input, Label, Spinner} from "reactstrap";
import React, {useState} from "react";
import {changePassword} from "../../api/Api";
import {useHistory} from "react-router-dom";
import {useSelector} from "react-redux";
import {selectLogin} from "./loginSlice";

export const ChangePwd = () => {

    const [oldNewPass, setOldNewPass] = useState({
        oldPassword: '',
        newPassword: '',
        repeatNewPassword: ''
    })
    const [step, setStep] = useState('')
    const [error, setError] = useState({})

    const updateField = e => {
        setOldNewPass({
            ...oldNewPass,
            [e.target.name]: e.target.value
        })
    }

    const pattern = /((?=.*\d)(?=.*[a-z]).{10,20})/

    const login = useSelector(selectLogin)

    const submitForm = e => {
        e.preventDefault()

        let valid = true

        let oldPasswordError = ''
        if (oldNewPass.oldPassword.length === 0) {
            oldPasswordError = 'This field is required'
            valid = false
        }

        let newPasswordError = ''
        if (!pattern.test(oldNewPass.newPassword)) {
            newPasswordError = 'Password must contain 1 digit and must be from 10 to 20 characters long'
            valid = false
        }

        let repeatNewPasswordError = ''
        if (oldNewPass.newPassword !== oldNewPass.repeatNewPassword) {
            repeatNewPasswordError = 'Passwords do not match'
            valid = false
        }

        setError({
            ...error,
            oldPassword: oldPasswordError,
            newPassword: newPasswordError,
            repeatNewPassword: repeatNewPasswordError
        })

        if (valid) {
            setStep('processing')
            changePassword({
                oldPassword: oldNewPass.oldPassword,
                newPassword: oldNewPass.newPassword
            }, login.user.token).then(
                () => history.replace("/"),
                () => setStep('fail'))
        }
    }

    const history = useHistory();

    const processing = step === 'processing'
    const fail = step === 'fail'

    return (
            <div className="container-fluid mt-3">
                <div className="row login-form">
                    <div className="col-10 offset-1 jumbotron">
                        <h3>Change your password</h3>
                        <Form onSubmit={submitForm}>
                            <FormGroup>
                                <Label>Old password</Label>
                                <Input type="password" name="oldPassword"
                                       value={oldNewPass.oldPassword} readOnly={processing}
                                       onChange={updateField} invalid={error.oldPassword}/>
                                {error.oldPassword && <FormFeedback>{error.oldPassword}</FormFeedback>}
                            </FormGroup>
                            <FormGroup>
                                <Label>New password</Label>
                                <Input type="password" id="newPassword" name="newPassword"
                                       value={oldNewPass.newPassword} readOnly={processing}
                                       onChange={updateField} invalid={error.newPassword} />
                                {error.newPassword && <FormFeedback>{error.newPassword}</FormFeedback>}
                            </FormGroup>
                            <FormGroup>
                                <Label>Repeat new password</Label>
                                <Input type="password" id="repeatNewPassword" name="repeatNewPassword"
                                       value={oldNewPass.repeatNewPassword} readOnly={processing}
                                       onChange={updateField} invalid={error.repeatNewPassword} />
                                {error.repeatNewPassword && <FormFeedback>{error.repeatNewPassword}</FormFeedback>}
                            </FormGroup>
                            <Button color="primary" block disabled={processing}>Submit</Button>
                        </Form>

                        {fail &&
                            <Alert color="danger" className="mt-3">Password change failed</Alert>
                        }

                        {processing &&
                            <div className="d-flex justify-content-center">
                                <Spinner className="mt-3"/>
                            </div>
                        }
                    </div>
                </div>
            </div>
        )
}
