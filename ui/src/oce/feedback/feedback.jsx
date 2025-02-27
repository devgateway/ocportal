import {
  Form, InputGroup,
} from 'react-bootstrap';
import React from 'react';
import ReCAPTCHA from 'react-google-recaptcha';
import { getFeedbackUrlPart } from './util';

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
      sendFeedbackDisabled: true,
    };
  }

  handleChange(e) {
    const { name } = e.target;
    const { value } = e.target;

    this.setState({
      [name]: value,
      error: false,
      changeNeverFired: false,
    });

    if (name === 'email') {
      this.setState((state) => ({
        emailValid: value && value.match(state.emailPattern),
      }));
    }
  }

  handleReplyClick() {
    this.setState({
      replyOpen: true,
    });
  }

  onChange(value) {
    this.setState({
      sendFeedbackDisabled: false,
      recaptchaResponse: value,
    });
  }

  submit() {
    const {
      email, emailPattern, name, comment, replyFor, recaptchaResponse,
    } = this.state;
    let error = false;
    // const [purchaseReqId, tenderTitle] = this.props.route;

    if (!email.match(emailPattern) || !name) {
      error = true;

      this.setState({
        emailValid: email.match(emailPattern),
      });
    }

    if (error) {
      this.setState({ error });
      return;
    }
    this.setState({
      replyOpen: false,
      email: '',
      name: '',
      comment: '',
      sendFeedbackDisabled: true,
    });
    this.props.feedbackPoster({
      email,
      name,
      url: getFeedbackUrlPart(),
      comment,
      replyFor,
      recaptchaResponse,
    });
  }

  validateTxt(stateField) {
    if (!this.state.changeNeverFired && stateField === '') {
      return 'error';
    }
    return 'success';
  }

  validateEmail() {
    if (this.state.emailValid === true) {
      return undefined;
    }

    const emailValid = this.state.email.match(this.state.emailPattern);

    if (emailValid) {
      return 'success';
    }
    return 'error';
  }

  disableFeedbackButton() {
    this.setState({
      sendFeedbackDisabled: true,
    });
  }

  renderReplyButton() {
    return (
      <button className="btn" onClick={this.handleReplyClick.bind(this)}>
        {this.state.replyFor ? 'Reply' : 'Add Comment'}
      </button>
    );
  }

  renderForm() {
    return (
      <div className="col-md-6">
        <Form.Group validationState={this.validateEmail()}>
          <Form.Label>Email</Form.Label>
          <InputGroup>
            <InputGroup.Text>@</InputGroup.Text>
            <Form.Control
              type="email"
              name="email"
              maxLength={255}
              value={this.state.email}
              placeholder="Email address"
              onChange={this.handleChange.bind(this)}
            />
          </InputGroup>
          <Form.Control.Feedback />
          {
            this.state.emailValid
              ? null
              : <Form.Text className="text-danger">Email is invalid</Form.Text>
          }
        </Form.Group>

        <Form.Group validationState={this.validateTxt(this.state.name)}>
          <Form.Label>Name</Form.Label>
          <Form.Control
            name="name"
            maxLength={255}
            value={this.state.name}
            placeholder="Name"
            onChange={this.handleChange.bind(this)}
          />
          <Form.Control.Feedback />
          {
            this.state.changeNeverFired || this.state.name ? null
              : <Form.Text className="text-danger">Please add a name</Form.Text>
          }
        </Form.Group>

        <Form.Group validationState={this.validateTxt(this.state.comment)}>
          <Form.Label>Comment</Form.Label>
          <Form.Control
            required
            as="textarea"
            name="comment"
            maxLength={10000}
            rows={5}
            value={this.state.comment}
            placeholder="Comment"
            onChange={this.handleChange.bind(this)}
          />
          <Form.Control.Feedback />
          {
            this.state.changeNeverFired || this.state.comment ? null
              : <Form.Text className="text-danger">Please add a comment</Form.Text>
          }
        </Form.Group>
        <ReCAPTCHA
          ref="recaptcha"
          sitekey="6LfRjM8UAAAAAABlMlrHAsC5Sgm0YjzlfRmppcVp"
          onChange={this.onChange.bind(this)}
          onExpired={this.disableFeedbackButton.bind(this)}
        />
        <div className="row apply-button">
          <div className="col-md-6">
            <button
              className="btn btn-default"
              type="submit"
              disabled={this.state.sendFeedbackDisabled}
              onClick={this.submit.bind(this)}
            >
              Send Feedback
            </button>
          </div>
        </div>
      </div>
    );
  }

  render() {
    return (
      <div>
        {this.state.replyOpen ? this.renderForm() : this.renderReplyButton()}
      </div>
    );
  }
}

export default FeedbackMessageForm;
