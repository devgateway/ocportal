import Header from '../layout/header';
import {Alert, FormControl, FormGroup, ControlLabel, HelpBlock} from 'react-bootstrap';
import {Typeahead} from 'react-bootstrap-typeahead';
import './alerts.scss';
import {useEffect, useState} from "react";
import {fetch, subscribeToAlerts} from "../api/Api";

const emailPattern = /^([\w.%+-]+)@([\w-]+\.)+([\w]{2,})$/i;

const Alerts = props => {

  const [fetchedDepartments, setFetchedDepartments] = useState([]);
  const [fetchedItems, setFetchedItems] = useState([]);
  const [serverResponse, setServerResponse] = useState();
  const [showFormErrors, setShowFormErrors] = useState(false);
  const [formData, setFormData] = useState({
    email: '',
    departments: [],
    items: []
  });
  const { departments, items, email } = formData;

  const [purchaseReqId, tenderTitle] = props.route;

  useEffect(() => {
    if (!purchaseReqId) {
      fetch('/makueni/filters/departments/all')
        .then(data => setFetchedDepartments(data));

      fetch('/makueni/filters/items/all')
        .then(data => setFetchedItems(data));
    }

    window.scrollTo(0, 0);
  }, [purchaseReqId]);

  const handleChange = e => {
    const {name, value} = e.target;
    setFormData(formData => ({
      ...formData,
      [name]: value
    }));
    setShowFormErrors(false);
  };

  const emailValid = email.match(emailPattern);

  const getEmailValidationState = () => {
    if (!email) {
      return null;
    } else if (emailValid) {
      return 'success';
    } else {
      return 'error';
    }
  }

  const formValid =
    (purchaseReqId || departments.length > 0 || items.length > 0)
    && emailValid;

  const submit = () => {
    setShowFormErrors(true);

    if (formValid) {
      setServerResponse(undefined);

      subscribeToAlerts({
        email: email,
        departments: departments.map(item => item.id),
        items: items.map(item => item.id),
        purchaseReqId: purchaseReqId
      }).then(data => setServerResponse(data));
    }
  }

  return (
    <div className="container-fluid dashboard-default">

      <Header translations={props.translations} onSwitch={props.onSwitch}
              styling={props.styling} selected=""/>

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
              <FormGroup validationState={getEmailValidationState()} bsSize={'large'}>
                <ControlLabel>Enter your email address</ControlLabel>
                <FormControl
                  type="email"
                  name="email"
                  value={email}
                  placeholder="email address"
                  onChange={handleChange}
                />
                <FormControl.Feedback/>
                {
                  email && !emailValid && <HelpBlock>Email is invalid</HelpBlock>
                }
              </FormGroup>
            </div>
          </div>

          {
            purchaseReqId !== undefined && tenderTitle !== undefined
              ? <div>
                <div className="row">
                  <div className="col-md-12">
                    <h4 className="sub-title">Alert Preferences</h4>
                  </div>
                </div>

                <div className="row">
                  <div className="col-md-12">
                    You will receive Email Updates for the following
                    Tender: <b>{unescape(tenderTitle)}</b>
                  </div>
                </div>
              </div>

              : <div>
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
                               onChange={(selected) => handleChange({
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
                               onChange={(selected) => handleChange({
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
              </div>
          }

          <div className="row apply-button">
            <div className="col-md-6">
              <button className="btn btn-info btn-lg submit" type="submit"
                      onClick={submit}>Apply Subscription Preferences
              </button>
            </div>
          </div>

          {
            showFormErrors && !formValid
              ? <div className="row validation-message">
                <div className="col-md-12">
                  <Alert bsStyle="danger">
                    <i className="glyphicon glyphicon-exclamation-sign"></i>&nbsp;
                    {
                      purchaseReqId === undefined
                        ? <span>Please enter a valid email address and select at least 1 Department or 1 Item</span>
                        : <span>Please enter a valid email address</span>
                    }
                  </Alert>
                </div>
              </div>
              : null
          }

          {
            (serverResponse !== undefined)
              ? <div className="row validation-message">
                <div className="col-md-12">
                  <h4>
                    {serverResponse.status === true
                      ? <Alert bsStyle="info">
                        A confirmation email was send to {email} address.
                        <br/>
                        Please check your email and click on provided URL in order to validate your
                        email address.
                      </Alert>
                      : <Alert bsStyle="danger">
                        Error subscribing!
                        <br/>
                        {serverResponse.status === false ? serverResponse.message : 'Unknown error'}
                      </Alert>
                    }
                  </h4>
                </div>
              </div>
              : null
          }
        </div>
      </div>
    </div>
  );

  return null;
}

export default Alerts;
