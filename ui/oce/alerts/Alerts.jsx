import CRDPage from '../corruption-risk/page';
import Header from '../layout/header';
import { Alert, ControlLabel, FormControl, FormGroup, HelpBlock } from 'react-bootstrap';
import { Typeahead } from 'react-bootstrap-typeahead';

import './alerts.less';
import { API_ROOT, OCE } from '../state/oce-state';
import { supplierFilters } from '../corruption-risk/suppliers/single/state';
import { SupplierTableState } from '../corruption-risk/suppliers/single/table/state';

class Alerts extends CRDPage {
  
  constructor(props) {
    super(props);
    
    this.state = {
      departments: [],
      fetchedDepartments: [],
      items: [],
      fetchedItems: [],
      emailPatter: /^([\w.%+-]+)@([\w-]+\.)+([\w]{2,})$/i,
      email: '',
      emailValid: true,
      error: false
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
      mapper: data => this.setState({ fetchedDepartments: data })
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
      mapper: data => this.setState({ fetchedItems: data })
    });
    
    
    this.handleChange = this.handleChange.bind(this);
    this.submit = this.submit.bind(this);
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
    
    this.setState({
      [name]: value,
      error: false
    });
    
    if (name === 'email') {
      this.setState({
        emailValid: this.state.email.match(this.state.emailPatter)
      });
    }
  }
  
  submit() {
    const { departments, items, email, emailPatter } = this.state;
    let error = false;
    
    // more validations
    if ((departments === undefined || departments.length === 0)
      && (items === undefined || items.length === 0)) {
      error = true;
    }
    if (!email.match(emailPatter)) {
      error = true;
      
      this.setState({
        emailValid: email.match(emailPatter)
      });
    }
    
    if (error) {
      this.setState({ error });
      
      return 0;
    } else {
      console.log('>>> SENDING!');
      
      
      this.subscribeAlertEP = this.alertsState.input({
        name: 'subscribeAlertEP',
        initial: `${API_ROOT}` + '/makueni/alerts/subscribeAlert',
      });
  
      this.subscribeAlertData = this.alertsState.input({
        name: 'subscribeAlertData',
        initial: {
          email: this.state.email,
          departments: this.state.departments.map(item => item.id),
          items: this.state.items.map(item => item.id)
        }
      });
  
      this.subscribeAlertURL = this.alertsState.remote({
        name: 'subscribeAlertURL',
        url: this.subscribeAlertEP,
        params: this.subscribeAlertData,
      });
  
      this.subscribeAlertResponse = this.alertsState.mapping({
        name: 'subscribeAlertResponse',
        deps: [this.subscribeAlertURL],
        mapper: data => {
          console.log(data);
        }
      });
  
      this.subscribeAlertResponse.getState('SubscribeAlerts');
    }
  }
  
  
  render() {
    const { departments, fetchedDepartments, items, fetchedItems, error } = this.state;
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
                <FormGroup validationState={this.validateEmail()} bsSize={'large'}>
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
              <div className="col-md-2"></div>
              <div className="col-md-5">
                <ControlLabel>Receive alerts for all Tenders from this Department</ControlLabel>
              </div>
              <div className="col-md-5">
                <ControlLabel>Receive alerts for these Items from all Departments</ControlLabel>
              </div>
            </div>
            <div className="row">
              <div className="col-md-2">
                When a New Tender is released
              </div>
              <div className="col-md-5">
                <Typeahead id='departments'
                           onChange={(selected) => this.handleChange({
                             target: {
                               name: 'departments',
                               value: selected
                             }
                           })}
                           options={fetchedDepartments === undefined ? [] : fetchedDepartments}
                           clearButton={true}
                           placeholder={'For this Department(s)'}
                           selected={departments}
                           multiple={true}
                           isLoading={fetchedDepartments === undefined}
                           bsSize={'large'}
                           highlightOnlyResult={true}
                />
              </div>
              <div className="col-md-5">
                <Typeahead id='items'
                           onChange={(selected) => this.handleChange({
                             target: {
                               name: 'items',
                               value: selected
                             }
                           })}
                           options={fetchedItems === undefined ? [] : fetchedItems}
                           clearButton={true}
                           placeholder={'For this Item(s)'}
                           selected={items}
                           multiple={true}
                           isLoading={fetchedItems === undefined}
                           bsSize={'large'}
                           highlightOnlyResult={true}
                />
              </div>
            </div>
            
            <div className="row apply-button">
              <div className="col-md-6">
                <button className="btn btn-info btn-lg submit" type="submit"
                        onClick={this.submit}>Apply Subscription Preferences
                </button>
              </div>
            </div>
            
            {
              error
                ? <div className="row validation-message">
                  <div className="col-md-12">
                    <Alert bsStyle="danger">
                      <i className="glyphicon glyphicon-exclamation-sign"></i>&nbsp;
                      Please enter a valid email addres and select at least 1 Department or 1 Item
                    </Alert>
                  </div>
                </div>
                : null
            }
          </div>
        </div>
      </div>
    );
  }
}

export default Alerts;
