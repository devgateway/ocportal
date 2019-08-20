import CRDPage from '../corruption-risk/page';
import Header from '../layout/header';
import { ControlLabel, FormControl, FormGroup, HelpBlock } from 'react-bootstrap';
import { Typeahead } from 'react-bootstrap-typeahead';

import './alerts.less';
import { API_ROOT, OCE } from '../state/oce-state';

class Alerts extends CRDPage {
  
  constructor(props) {
    super(props);
    
    this.state = {
      departments: [],
      departmentsSelected: [],
      items: [],
      itemsSelected: [],
      emailPatter: /^([\w.%+-]+)@([\w-]+\.)+([\w]{2,})$/i,
      email: '',
      emailValid: true
    };
    
    /* ************************ create the state for Selectors ************************ */
    this.alertsState = OCE.substate({ name: 'Alerts' });
    
    this.depEP = this.alertsState.input({
      name: 'filterDepEp',
      initial: `${API_ROOT}` + '/makueni/filters/departments/all',
    });
    
    this.depRemote = this.alertsState.remote({
      name: 'filterDepRemote',
      url: this.depEP,
    });
    
    this.depData = this.alertsState.mapping({
      name: 'filterDepData',
      deps: [this.depRemote],
      mapper: data => this.setState({ departments: data })
    });
    
    
    this.itemEP = this.alertsState.input({
      name: 'filterItemEp',
      initial: `${API_ROOT}` + '/makueni/filters/items/all',
    });
    
    this.itemRemote = this.alertsState.remote({
      name: 'filterItemRemote',
      url: this.itemEP,
    });
    
    this.itemData = this.alertsState.mapping({
      name: 'filterItemData',
      deps: [this.itemRemote],
      mapper: data => this.setState({ items: data })
    });
    
    
    this.handleChange = this.handleChange.bind(this);
  }
  
  componentDidMount() {
    this.depData.getState('Alerts');
    this.itemData.getState('Alerts');
  }
  
  validateEmail() {
    if (this.state.emailValid === true) {
      return undefined;
    }
    
    const emailValid = this.state.email.match(this.state.emailPatter);
    
    if (emailValid) {
      return 'success';
    } else {
      return 'error';
    }
  }
  
  handleChange(e) {
    const name = e.target.name;
    const value = e.target.value;
    
    console.log(name + ' - ' + value);
    
    this.setState({
      [name]: value
    });
    
    if (name === 'email') {
      this.setState({
        emailValid: this.state.email.match(this.state.emailPatter)
      });
    }
  }
  
  
  render() {
    const { departments, departmentsSelected, items, itemsSelected } = this.state;
    
    return (<div className="container-fluid dashboard-default">
        
        <Header translations={this.props.translations} onSwitch={this.props.onSwitch}
                styling={this.props.styling} selected=""/>
        
        <div className="alerts content row">
          <div className="col-md-10 col-md-offset-1">
            <div className="row title">
              <div className="col-md-12">
                <h2>Subscribe to Makueni OC Portal Alerts</h2>
              </div>
            </div>
            
            <div className="row">
              <div className="col-md-12">
                <h4 className="sub-title">Sign up for Alerts</h4>
              </div>
              
              <div className="col-md-6">
                <FormGroup validationState={this.validateEmail()} bsSize={"large"}>
                  <ControlLabel>Enter your email address</ControlLabel>
                  <FormControl
                    type="email"
                    name="email"
                    value={this.state.email}
                    placeholder="email address"
                    onChange={this.handleChange}
                  />
                  <FormControl.Feedback/>
                  {
                    this.state.emailValid
                      ? null
                      : <HelpBlock>Email is invalid</HelpBlock>
                  }
                </FormGroup>
              </div>
            </div>
            
            <div className="row">
              <div className="col-md-12">
                <h4 className="sub-title">Alert Preferences</h4>
              </div>
              <div className="col-md-2">
                When a New Tender is released
              </div>
              <div className="col-md-5">
                <Typeahead id='departments'
                           name={'departments'}
                           onChange={this.handleChange}
                           options={departments === undefined ? [] : departments}
                           clearButton={true}
                           placeholder={'For this Department(s)'}
                           selected={departmentsSelected}
                           multiple={true}
                           isLoading={departments === undefined}
                           bsSize={"large"}
                           highlightOnlyResult={true}
                />
              </div>
              <div className="col-md-5">
                <Typeahead id='items'
                           name={'items'}
                           onChange={this.handleChange}
                           options={items === undefined ? [] : items}
                           clearButton={true}
                           placeholder={'For this Item(s)'}
                           selected={itemsSelected}
                           multiple={true}
                           isLoading={items === undefined}
                           bsSize={"large"}
                           highlightOnlyResult={true}
                />
              </div>
            </div>
            
            <div className="row apply-button">
              <div className="col-md-6">
                <button className="btn btn-info btn-lg submit" type="submit"
                        onClick={() => console.log('Submit!')}>Apply Subscription Preferences
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

export default Alerts;
