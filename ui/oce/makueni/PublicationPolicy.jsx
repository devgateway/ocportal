import CRDPage from '../corruption-risk/page';
import Header from '../layout/header';
import './makueni.less';
import React from 'react';

class Docs extends CRDPage {


  render() {
    return (<div className="container-fluid dashboard-default">
      <Header translations={this.props.translations} onSwitch={this.props.onSwitch}
              styling={this.props.styling} selected="docs"/>
      <div className="makueni-procurement-plan content row">

        <div className="col-md-2 col-md-offset-5">
          <h1>Publication Policy</h1>
        </div>

        <div className="col-md-6 col-md-offset-3 text-justify">
          <h3>Purpose of publication</h3>
          The data in this portal covers the Government of Makueni County (GMC) procurement and contracting data. Publication of this data is aimed to help the Government of Makueni County and citizens to monitor procurement efficiency, market competitiveness, and corruption risk, among other use cases. The portal also seeks to foster collaboration that strengthens the capacity of the county government and Makueni citizens to engage with procurement data, and creates an enabling environment for procurement to deliver more effectively for citizens. The data is modelled according to the Open Contracting Data Standard (OCDS) (http://standard.open-contracting.org/latest/en/).
          <h3>Publication Details</h3>
          Makueni Open Contracting Data are refreshed every weekend, which pulls approved data entered into the Open Contracting Portal and converts it into OCDS 1.1 and releases the JSON and updates the data in the analytics dashboards.
          <div>
            <img src="assets/ocmakueni-architecture.png"/>
            Government of Makueni County Open Contracting Portal Architecture
          </div>
          <h3>Accessing the Data</h3>
          OCDS Data can be accessed <a className="download-file" href="https://opencontracting.makueni.go.ke/api/ocds/package/all"> through a bulk JSON export.</a>
          As well as by accessing <a className="download-file" href="https://opencontracting.makueni.go.ke/swagger-ui.html#/ocds-controller">the specific API endpoints here</a>
          <p/>
          The Government of Makueni County provides an open contracting portal to support exploration and use of contracting data at the following url:
          <a className="download-file" href="https://opencontracting.makueni.go.ke">opencontracting.makueni.go.ke</a>. The portal provides access to all contracting data through a series of filters including a free text search, analyzes and visualizes the data in a series of charts, including a GIS feature.
          <p/>
          Data tables on the main page include data that is not available in the OCDS exports as OCDS does not cover all data relevant for Makueni County. An Excel export on the portal includes the full data set. Both datasets will be maintained going forward.
          <p/>
          The table in the Annex provides the full list of data fields collected and published by the Makeuni Open Contracting Portal and the OCDS path for those converted to OCDS.

          <h3>Data Scope</h3>

          <ul>
            <li><b>Dates:</b> 2019-01-01 onwards</li>
            <li><b>Buyers:</b> All Makueni County Departments</li>
            <li><b>Values:</b> All Makueni County Contracts</li>
            <li><b>Process types:</b> Direct, open tender national, open tender international, RF proposals, RFQs (below 2,000,000KES for goods and services and below 4,000,000KES for works) restricted tenders, special permitted, low fall procurement (below 30,000KES for goods and services and below 50,000KES for works).</li>
            <li><b>Stages:</b> We publish data for all steps of the procurement and implementation phase starting with the budget and procurement plan, tender, award, contract, implementation reports, and payment.</li>
            <li><b>Change History:</b> A change history is not currently recorded in the OCDS export.</li>
          </ul>

          <h3>Licensing</h3>
          The data is made available to the public under a
          <a className="download-file" href="https://creativecommons.org/licenses/by-sa/4.0/"> Creative Commons Attribution Share-Alike 4.0 (CC-BY-SA-4.0) license</a>. This grants any user the freedom to explore, download, monitor and re-use the data for any purposes, including contract monitoring, analytics and research. The Creative Commons Attribution Share-Alike license allows re-distribution and re-use of a licensed work on the conditions that the creator is appropriately credited and that any derivative work is made available under “the same, similar or a compatible license”.
          <h3>Future development plans</h3>
          The Government of Makueni County encourages users from all sectors of society (government, private sector, civil society) to visit the website regularly, subscribe to updates on tender opportunities, and put this data into use. The county government is committed to improving the quality of the disclosed data over time, and welcomes any feedback from users on data quality. The Government of Makueni County is working to introduce implementation data that will include the monitoring of project quality.
          <h3>Publisher contact information</h3>
          If you have questions, comments or ideas, please get in touch with us at <a className="download-file" href="mailto:opencontracting@makueni.go.ke">opencontracting@makueni.go.ke</a>.
          You can subscribe to updates on tender opportunities and information on specific tenders.

          <h3>Disclaimer</h3>
          The Government of Makueni County cannot guarantee that data provided by the different entities contributing to OCDS data is kept up-to-date.
          The GMC is not responsible for the decisions taken from the information disseminated in the open contracting portal or for possible damages
          caused to the visiting user or to third parties due to actions that are based solely on the information obtained on the site.
          The GMC may carry out, at any time and without prior notice, modifications and updates on the information contained in the open contracting portal or its configuration or presentation.

          <h3>Data and Extensions</h3>
          The following OCDS extension have been used and created for this data:<p/>

          <a className="download-file"
             href="https://github.com/devgateway/forms-makueni/tree/master/persistence-mongodb/src/main/resources/extensions/contract_contractor">
            Contract Contractor</a>
          <p/>

          <a className="download-file"
             href="https://github.com/devgateway/forms-makueni/tree/master/persistence-mongodb/src/main/resources/extensions/first_time_winners">
            First Time Winners</a>
          <p/>
          <a className="download-file"
             href="https://github.com/devgateway/forms-makueni/tree/master/persistence-mongodb/src/main/resources/extensions/fiscal_year">
            Fiscal Year</a>
          <p/>
          <a className="download-file"
             href="https://github.com/devgateway/forms-makueni/tree/master/persistence-mongodb/src/main/resources/extensions/milestone_delayed_authorization">
            Delayed Milestone</a>
          <p/>
          <a className="download-file"
             href="https://github.com/devgateway/forms-makueni/tree/master/persistence-mongodb/src/main/resources/extensions/planning_items">
            Planning Items</a>
          <p/>
          <a className="download-file"
             href="https://github.com/devgateway/forms-makueni/tree/master/persistence-mongodb/src/main/resources/extensions/target_groups">
            Target Groups</a>
          <p/>
          <a className="download-file"
             href="https://github.com/devgateway/forms-makueni/tree/master/persistence-mongodb/src/main/resources/extensions/tender_location">
            Tender Location</a>
          <p/>
          <a className="download-file"
             href="https://github.com/open-contracting-extensions/ocds_bid_extension">
            OCDS Bid Extension</a>
          <p/>
          <a className="download-file"
             href="https://github.com/open-contracting-extensions/ocds_budget_breakdown_extension">
            OCDS Budget Breakdown Extensions</a>
          <p/>
          <a className="download-file"
             href=" https://github.com/open-contracting-extensions/ocds_location_extension">
            OCDS Location Extension</a>
          <h3>Makueni System To OCDS Mapping</h3>
          We have created an <a className="download-file" href="assets/Makueni-OCDS-Conversion-Matrix.xlsx">Excel spreadsheet</a> outlining how the data maps to OCDS, and <a target="_blank" className="download-file" href="assets/javadoc/dao/index.html">a separate page for the non-OCDS fields</a>.
        </div>


      </div>
    </div>);
  }
}

export default Docs;
