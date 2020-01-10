import { ControlLabel, FormControl, FormGroup, HelpBlock } from 'react-bootstrap';
import React from 'react';


class FeedbackMessages extends React.PureComponent {

  constructor(props) {
    super(props);

    this.state = {
      emailPattern: /^([\w.%+-]+)@([\w-]+\.)+([\w]{2,})$/i,
      email: '',
      name: '',
      emailValid: true,
      error: false
    };
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

  handleChange (e) {
    const name = e.target.name;
    const value = e.target.value;

    this.setState({
      [name]: value,
      error: false
    });

    if (name === 'email') {
      this.setState({
        emailValid: this.state.email.match(this.state.emailPattern)
      });
    }
  }

  render() {
    return(
    <div className="col-md-2">
      <FormGroup validationState={this.validateEmail()}>
        <ControlLabel>Email</ControlLabel>
        <FormControl
          type="email"
          name="email"
          value={this.state.email}
          placeholder="email address"
          onChange={this.handleChange.bind(this)}
        />
        <FormControl.Feedback/>
        {
          this.state.emailValid
            ? null
            : <HelpBlock>Email is invalid</HelpBlock>
        }
      </FormGroup>

      <FormGroup>
        <ControlLabel>Name</ControlLabel>
        <FormControl
          required
          name="name"
          value={this.state.name}
          placeholder="Name"
          onChange={this.handleChange.bind(this)}
        />
        <FormControl.Feedback/>
      </FormGroup>
    </div>);
  }
}
export default FeedbackMessages;

