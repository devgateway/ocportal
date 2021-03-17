import React from 'react';
import CRDPage from '../corruption-risk/page';
import Header from '../layout/header';
import './makueni.scss';
import Footer from '../layout/footer';
import smsHelp from '../resources/help/sms-help.png';
import ussdMainMenu from '../resources/help/ussd-main-menu.jpg';
import ussdSelectSubcounty from '../resources/help/ussd-select-subcounty.jpg';
import ussdSelectWard from '../resources/help/ussd-select-ward.jpg';
import ussdSubscribed from '../resources/help/ussd-subscribed.jpg';

class SMSHelp extends CRDPage {
  render() {
    return (
      <div className="container-fluid dashboard-default">
        <Header
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
                implementation status of a particular tender -  M&E report and the PMC report status.
                Users can use the SMS portal to:
              </p>
              <p>
                <ol>
                  <li>Subscribe to receiving alerts relevant to your ward or sub-county</li>
                  <li>Receive implementation information on a particular contract and;</li>
                  <li>Report to the county on any implementation feedback</li>
                </ol>
              </p>
            </div>
          </div>
          <div className="row">
            <div className="col-md-8 col-md-offset-2">
              <h2>1. Subscribe to receiving alerts relevant to your ward or sub-county</h2>
              <p>
                Users can subscribe to receive SMS alerts once the M&E reports and PMC reports status are approved for
                projects in their sub-county or ward. To subscribe, follow the steps below:
              </p>
              <p>
                <ol>
                  <li>
                    {/* eslint-disable-next-line react/jsx-one-expression-per-line */}
                    Dial <code>*838*3#</code>
                  </li>
                  <li>
                    <p>Once  you type  in the code above, you will be prompted to select from the 3 options below:</p>
                    <img src={ussdMainMenu} alt="USSD Menu" className="img-fluid" />
                    <p>
                      <em>
                        The home screen has 3 options:
                        <ul>
                          <li>Subscribe - directs you to select the ward or sub-county to subscribe to</li>
                          <li>
                            View subscriptions or unsubscribe - directs you to view your subscriptions and
                            unsubscribe to all or one of the wards or sub-counties you have subscribed to.
                          </li>
                          <li>
                            Change language - gives you the options to change the language of alerts to
                            English or Kiswahili
                          </li>
                        </ul>
                      </em>
                    </p>
                  </li>
                  <li>
                    <p>
                      {/* eslint-disable-next-line react/jsx-one-expression-per-line */}
                      To subscribe, select Option <strong>1. Subscribe</strong>, and you will be directed to
                      select the sub-county you would like to subscribe to.
                      Select the sub-county name, by writing the number of the sub-county.
                      For example, I will write 1 to subscribe to Kaiti sub-county below.
                    </p>
                    <img src={ussdSelectSubcounty} alt="USSD Menu" className="img-fluid" />
                  </li>
                  <li>
                    <p>
                      After selecting the sub-county, you will be directed to the sub-county page where you can
                      either select all wards(select 1) in that sub-county or select a particular ward.
                    </p>
                    <img src={ussdSelectWard} alt="USSD Menu" className="img-fluid" />
                  </li>
                  <li>
                    <p>
                      Once selected ward(s), you will then receive a confirmation of your subscription
                      to the ward or sub-county
                    </p>
                    <img src={ussdSubscribed} alt="USSD Menu" className="img-fluid" />
                  </li>
                </ol>
              </p>
            </div>
          </div>
          <div className="row">
            <div className="col-md-8 col-md-offset-2">
              <h2>2. Requesting information about a specific contract</h2>
              <p>
                The tender code is the code used to request information on a particular tender.
                This code can be retrieved on the public portal page for each tender shown below.
              </p>
              <img src={smsHelp} alt="Public Portal Screenshot" className="img-fluid" />
              <p>
                For those who cannot access the public portal to retrieve the tender code, the County will
                communicate the code to the public through Project Management Committees and local authority offices.
              </p>
              <p>
                {/* eslint-disable-next-line react/jsx-one-expression-per-line */}
                To receive information on a specific contract, send an SMS to the number <strong>40014</strong> with
                the following format:
              </p>
              <p><code>CON INFO [Tender code]</code></p>

              <p>
                {/* eslint-disable-next-line react/jsx-one-expression-per-line */}
                For example, <kbd>CON INFO 41802</kbd>
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
              <h2>3. Report to the county on any implementation feedback</h2>
              <p>
                In order to send the county any feedback on a specific tender, send an SMS to the number
                40014 with the following format:
              </p>
              <p><code>CON REPORT [Tender code] [text]</code></p>

              <p>
                {/* eslint-disable-next-line react/jsx-one-expression-per-line */}
                For example, <kbd>CON REPORT 41802 work has been stopped for months</kbd>
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
        <Footer />
      </div>
    );
  }
}

export default SMSHelp;
