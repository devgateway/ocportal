import {Alert, Button, Form, FormGroup, Input, Label, Spinner} from "reactstrap";
import React, {useState} from "react";
import {recoverPassword} from "../../api/Api";
import {useHistory} from "react-router-dom";

export const RecoverPwd = () => {

    const [email, setEmail] = useState('')
    const [step, setStep] = useState('')

    const submitForm = e => {
        e.preventDefault()
        setStep('processing')
        recoverPassword({
            email: email
        }).then(() => {
            setStep('success')
        }, () => {
            setStep('fail')
        })
    }

    const history = useHistory();

    const goToLogin = e => {
        e.preventDefault()
        history.replace("/")
    }

    const processing = step === 'processing'
    const fail = step === 'fail'

    return (step === 'success')
        ? (
            <div className="container-fluid mt-3">
                <div className="row login-form">
                    <div className="col-10 offset-1 jumbotron">
                        <p>Please check your email, you will receive a link that will allow you to change your password.
                            It will expire in an hour. Thank you!</p>
                        <Button color="primary" block onClick={goToLogin}>Ok</Button>
                    </div>
                </div>
            </div>
        )
        : (
            <div className="container-fluid mt-3">
                <div className="row login-form">
                    <div className="col-10 offset-1 jumbotron">
                        <h2>Forgot your password</h2>
                        <Form onSubmit={submitForm}>
                            <FormGroup>
                                <Label>Please insert your email address</Label>
                                <Input type="text" name="email" placeholder="Email address"
                                       value={email} readOnly={processing}
                                       onChange={e => setEmail(e.target.value)} required/>
                            </FormGroup>
                            <Button color="primary" block disabled={processing}>Submit</Button>
                        </Form>

                        {fail &&
                            <Alert color="danger" className="mt-3">Password recovery failed</Alert>
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
