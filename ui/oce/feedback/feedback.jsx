import {FormControl, FormGroup, FormLabel, FormText, InputGroup} from 'react-bootstrap';
import React from 'react';
import {getFeedbackUrlPart} from './feedbackList';
import ReCAPTCHA from 'react-google-recaptcha';


class FeedbackMessageForm extends React.PureComponent {

  constructor(props) {
    super(props);
    this.state = {
      changeNeverFired: true,
      replyOpen: this.props.replyOpen,
      emailPattern: /^([\w.%+-]+)@([\w-]+\.)+([\w]{2,})$/i,
      email: '',
      name: '',
      comment: '',
      replyFor: this.props.replyFor,
      emailValid: true,
      error: false,
      recaptchaResponse: '',
      sendFeedbackDisabled: true
    };
  }

  validateTxt(stateField) {
    if (!this.state.changeNeverFired && stateField === '') {
      return 'error';
    } else {
      return 'success';
    }
  }


  validateEmail() {
    if (this.state.emailValid === true) {
      return undefined;
    }

    const emailValid = this.state.email.match(this.state.emailPattern);

    if (emailValid) {
      return 'success';
    } else {
      return 'error';
    }
  }

  handleChange(e) {
    const name = e.target.name;
    const value = e.target.value;

    this.setState({
      [name]: value,
      error: false,
      changeNeverFired: false
    });

    if (name === 'email') {
      this.setState({
        emailValid: value && value.match(this.state.emailPattern)
      });
    }
  }

  submit() {
    const { email, emailPattern, name, comment, replyFor, recaptchaResponse } = this.state;
    let error = false;
    //const [purchaseReqId, tenderTitle] = this.props.route;

    if (!email.match(emailPattern) || !name) {
      error = true;

      this.setState({
        emailValid: email.match(emailPattern)
      });
    }


    if (error) {
      this.setState({ error });
      return 0;
    } else {
      this.setState({
        replyOpen: false,
        email: '',
        name: '',
        comment: '',
        sendFeedbackDisabled: true,
      });
      this.props.feedbackPoster({
        email: email,
        name: name,
        url: getFeedbackUrlPart(),
        comment: comment,
        replyFor: replyFor,
        recaptchaResponse: recaptchaResponse
      });
    }
  }

  renderReplyButton() {
    return (<button className="btn" onClick={this.handleReplyClick.bind(this)}>
      {this.state.replyFor ? 'Reply' : 'Add Comment'}
    </button>);
  }

  handleReplyClick() {
    this.setState({
      replyOpen: true
    });
  }

  onChange(value) {
    this.setState({
      sendFeedbackDisabled: false,
      recaptchaResponse: value
    });
  }

  disableFeedbackButton() {
    this.setState({
      sendFeedbackDisabled: true,
    });
  }


  renderForm() {

    return (
      <div className="col-md-6">
        <FormGroup validationState={this.validateEmail()}>
          <FormLabel>Email</FormLabel>
          <InputGroup>
            <InputGroup.Addon>@</InputGroup.Addon>
            <FormControl
                type="email"
                name="email"
                maxLength={255}
                value={this.state.email}
                placeholder="Email address"
                onChange={this.handleChange.bind(this)}
            />
          </InputGroup>
          <FormControl.Feedback/>
          {
            this.state.emailValid
                ? null
                : <FormText>Email is invalid</FormText>
          }
        </FormGroup>

        <FormGroup validationState={this.validateTxt(this.state.name)}>
          <FormLabel>Name</FormLabel>
          <FormControl
              name="name"
              maxLength={255}
              value={this.state.name}
              placeholder="Name"
              onChange={this.handleChange.bind(this)}
          />
          <FormControl.Feedback/>
          {
            this.state.changeNeverFired || this.state.name ? null :
                <FormText>Please add a name</FormText>
          }
        </FormGroup>

        <FormGroup validationState={this.validateTxt(this.state.comment)}>
          <FormLabel>Comment</FormLabel>
          <FormControl
              required
              as="textarea"
              name="comment"
              maxLength={10000}
              rows={5}
              value={this.state.comment}
              placeholder="Comment"
              onChange={this.handleChange.bind(this)}
          />
          <FormControl.Feedback/>
          {
            this.state.changeNeverFired || this.state.comment ? null :
                <FormText>Please add a comment</FormText>
          }
        </FormGroup>
        <ReCAPTCHA
          ref="recaptcha"
          sitekey="6LfRjM8UAAAAAABlMlrHAsC5Sgm0YjzlfRmppcVp"
          onChange={this.onChange.bind(this)}
          onExpired={this.disableFeedbackButton.bind(this)}
        />
        <div className="row apply-button">
          <div className="col-md-6">
            <button className="btn btn-default" type="submit"
                    disabled={this.state.sendFeedbackDisabled}
                    onClick={this.submit.bind(this)}>Send Feedback
            </button>
          </div>
        </div>
      </div>);
  }

  render() {
    return (
      <div>
        {this.state.replyOpen ? this.renderForm() : this.renderReplyButton()}
      </div>);
  }
}

export default FeedbackMessageForm;

