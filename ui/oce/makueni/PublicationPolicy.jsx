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
          <h3> About the data</h3>
          The data in this portal covers the Government of Makueni County procurement and contracting data.
          Publication of this data is aimed to help the Government of Makueni County and citizens to monitor procurement efficiency,
          market competitiveness, and corruption risk, among other use cases. The portal also seeks to foster collaboration that strengthens
          the capacity of the county government and Makueni citizens to engage with procurement data, and creates an enabling environment for
          procurement to deliver more effectively for citizens. The data is modelled according to the <a className="download-file" href="https://standard.open-contracting.org/1.1/en/">Open Contracting Data Standard (OCDS)</a>.
          <h3>Licensing</h3>
          The data is made available to the public under a <a className="download-file" href="https://opendefinition.org/licenses/cc-by-sa/">Creative Commons Attribution Share-Alike 4.0 (CC-BY-SA-4.0) license</a>. This grants any user the freedom to explore, download, monitor and re-use the data for any purposes, including contract monitoring, analytics and research. The Creative Commons Attribution Share-Alike license allows re-distribution and re-use of a licensed work on the conditions that the creator is appropriately credited and that any derivative work is made available under "the same, similar or a compatible license".
          <h3>Future development plans</h3>
          The Government of Makueni County encourages users from all sectors of society (government, private sector, civil society) to visit the website regularly, subscribe to updates on tender opportunities, and put this data into use. The county government is committed to improving the quality of the disclosed data over time, and welcomes any feedback from users on data quality. The Government of Makueni County is working to introduce implementation data that will include the monitoring of project quality.
          <h3>Publisher contact information</h3>
          If you have questions, comments or ideas, please get in touch with us. General inquiries can be submitted to  or through the online form. You can subscribe to updates on tender opportunities and information on specific tenders.
        </div>


      </div>
    </div>);
  }
}

export default Docs;
