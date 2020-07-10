import CRDPage from '../corruption-risk/page';
import Header from '../layout/header';
import './makueni.less';
import React from 'react';
import Footer from '../layout/footer';

class Docs extends CRDPage {


  render() {
    return (<div className="container-fluid dashboard-default">
      <Header translations={this.props.translations} onSwitch={this.props.onSwitch}
              styling={this.props.styling} selected="docs"/>
      <div className="makueni-procurement-plan content row">

        <div className="col-md-2 col-md-offset-5">
          <h1>Resources</h1>
        </div>

        <div className="col-md-6 col-md-offset-3">
          <div className="list-group">
            <a href="#!/portal-videos" className="list-group-item">
              <h3 className="list-group-item-heading">Portal Walkthrough and Videos</h3>
            </a>
            <a href="/swagger-ui.html" className="list-group-item">
              <h3 className="list-group-item-heading">All API Endpoints</h3>
              <p className="list-group-item-text">Access portal data in JSON Format. This is a list all API Endpoints available for this portal.</p>
            </a>
            <a href="/swagger-ui.html#/makueni-data-controller" className="list-group-item">
              <h3 className="list-group-item-heading">Makueni Specific Endpoints</h3>
              <p className="list-group-item-text">Access portal data in JSON Format. API Endpoints Specific to Government of Makueni County Open Contracting Portal implementation
              </p>
            </a>
            <a href="/swagger-ui.html#/ocds-controller" className="list-group-item">
              <h3 className="list-group-item-heading">Open Contracting Data Standard (OCDS) Endpoints</h3>
              <p className="list-group-item-text">Access portal data in JSON Format. API Endpoints Specific to OCDS implemented for this portal
              </p>
            </a>
            <a href="https://standard.open-contracting.org/1.1/en/" className="list-group-item">
              <h3 className="list-group-item-heading">Open Contracting Data Standard (OCDS) version 1.1 Documentation</h3>
              <p className="list-group-item-text">The Open Contracting Data Standard (OCDS) enables disclosure of data and documents at all stages of the contracting process by defining a common data model. It was created to support organizations to increase contracting transparency, and allow deeper analysis of contracting data by a wide range of users.
              </p>
            </a>
            <a href="https://github.com/devgateway/forms-makueni" className="list-group-item">
              <h3 className="list-group-item-heading">Government of Makueni County Open Contracting Portal Source Code</h3>
              <p className="list-group-item-text">Access the source code of the portal, on Github
              </p>
            </a>
            <a href="https://github.com/devgateway/forms-makueni/blob/develop/LICENSE" className="list-group-item">
              <h3 className="list-group-item-heading">License</h3>
              <p className="list-group-item-text">Government of Makueni County Open Contracting Portal is an open source project released under the MIT License.
              </p>
            </a>
            <a href="#!/publication-policy" className="list-group-item">
              <h3 className="list-group-item-heading">Publication Policy</h3>
              <p className="list-group-item-text">Publication Policy for data available through the Open Contracting Portal.
              </p>
            </a>
          </div>
        </div>
      </div>
      <Footer/>
    </div>);
  }
}

export default Docs;
