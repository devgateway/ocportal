import CRDPage from '../corruption-risk/page';
import Header from '../layout/header';
import './makueni.scss';
import React from 'react';
import Footer from '../layout/footer';
import smsHelp from '../resources/help/sms-help.png';

class SMSHelp extends CRDPage {
  render() {
    return (
      <div className="container-fluid dashboard-default">
        <Header
          translations={this.props.translations}
          onSwitch={this.props.onSwitch}
          styling={this.props.styling}
          selected="smshelp"
        />
        <div className="makueni-procurement-plan content">
          <div className="row">
            <div className="col-md-8 col-md-offset-2 makueni-form">
              <a href="#" onClick={() => window.history.back()} className="back-link">
                <span className="back-icon">
                  <span className="previous">&#8249;</span>
                </span>
                <span className="back-text">
                  Go Back
                </span>
              </a>
            </div>
          </div>

          <div className="row">
            <div className="col-md-8 col-md-offset-2">
              <h1>The Government of Makueni County Open Contracting SMS Portal</h1>
            </div>
          </div>
          <div className="row">
            <div className="col-md-8 col-md-offset-2">
          &nbsp;
            </div>
          </div>

          <div className="row">
            <div className="col-md-8 col-md-offset-2">
              <p>
                The Government of Makueni County SMS portal is a communication tool to the public on the
                implementation status of a particular tender -  M&E report and the PMC report status. Users can use the SMS portal to:
              </p>
              <p>
                <ol>
                  <li>Receive implementation information on a particular contract and;</li>
                  <li>Report to the county on any implementation feedback</li>
                </ol>
              </p>
              The tender code is the code used to request information on a particular tender.
              This code can be retrieved on the public portal page for each tender shown below:
            </div>
          </div>
          <div className="row">
            <div className="col-md-8 col-md-offset-2">
              <img src={smsHelp} width="80%" />
            </div>
          </div>
          <div className="row">
            <div className="col-md-8 col-md-offset-2">
              For those who cannot access the public portal to retrieve the tender code, the County
              will communicate the code to the public through Project Management Committees and local authority offices.
            </div>
          </div>
          <div className="row">
            <div className="col-md-8 col-md-offset-2">
              <h2>1. Requesting information about a specific contract</h2>
              <p>To receive information on a specific contract, send an SMS to the number 40014 with the following format:</p>
              <p><code>CON INFO [Tender code]</code></p>

              <p>
                For example,
                <kbd>CON INFO 41802</kbd>
              </p>

              Once the message is sent, you will receive an SMS like the one below:
              <p>
                <samp>
                  Tender Code: 41802
                  <br />
                  Tender Name: CONSTRUCTION OF 2 DOOR NO. PIT LATRINES AND URINAL AT KATIKOMU ECDE CENTER ILIMA WARD
                  <br />
                  M&E Status: Complete and in use
                  <br />
                  PMC Status: At risk
                  <br />
                </samp>
              </p>
            </div>
          </div>
          <div className="row">
            <div className="col-md-8 col-md-offset-2">
              <h2>2. Report to the county on any implementation feedback</h2>
              <p>In order to send the county any feedback on a specific tender, send an SMS to the number 40014 with the following format:</p>
              <p><code>CON REPORT [Tender code] [text]</code></p>

              <p>
                For example,
                <kbd>CON REPORT 41802 work has been stopped for months</kbd>
              </p>

              You will then receive an SMS reply to acknowledge the receipt of the feedback:
              <p>
                <samp>
                  Tender Code: 41802
                  <br />
                  Feedback recorded.
                  <br />
                  Thank you.
                  <br />
                </samp>
              </p>
              <p>
                The feedback will then be displayed on the public portal feedback section of the specified
                tender. Your phone number will not be visible on the portal. Once Makueni County receives
                your feedback, they will send you an SMS with a reply to your concerns.
              </p>
              <p>
                <samp>
                  Tender Code: 41802
                  <br />
                  Feedback reply: We confirm the work stopped because of budget allocation challenges.
                </samp>
              </p>
            </div>
          </div>
        </div>
        <Footer translations={this.props.translations} />
      </div>
    );
  }
}

export default SMSHelp;
