import CRDPage from '../corruption-risk/page';
import Header from '../layout/header';
import './makueni.less';
import React from 'react';

class PortalVideos extends CRDPage {


  render() {
    return (<div className="container-fluid dashboard-default">
      <Header translations={this.props.translations} onSwitch={this.props.onSwitch}
              styling={this.props.styling} selected="docs"/>
      <div className="makueni-procurement-plan content row">

        <div className="col-md-4 col-md-offset-5">
          <h1>Portal Walkthroughs & Videos</h1>
        </div>

        <div className="col-md-8 col-md-offset-4 text-justify">
          <h2>How to view and download tender data</h2>
          <iframe width="640" height="480" src="https://www.youtube.com/embed/ER9wfYw1dnw"
                  frameBorder="0"
                  allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture"
                  allowFullScreen></iframe>
        </div>

        <div className="col-md-8 col-md-offset-4 text-justify">
          <h2>How to send and receive feedback</h2>
          <iframe width="640" height="480" src="https://www.youtube.com/embed/bI4Yt_CKR_A"
                  frameBorder="0"
                  allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture"
                  allowFullScreen></iframe>
        </div>

        <div className="col-md-8 col-md-offset-4 text-justify">
          <h2>How to view and download procurement plans</h2>
          <iframe width="640" height="480" src="https://www.youtube.com/embed/wBiy17jijYo"
                  frameBorder="0"
                  allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture"
                  allowFullScreen></iframe>
        </div>

        <div className="col-md-8 col-md-offset-4 text-justify">
          <h2>How to access and view charts</h2>
          <iframe width="640" height="480" src="https://www.youtube.com/embed/7D1V9kqzQ-M"
                  frameBorder="0"
                  allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture"
                  allowFullScreen></iframe>
        </div>

        <div className="col-md-8 col-md-offset-4 text-justify">
          <h2>Resources and API information</h2>
          <iframe width="640" height="480" src="https://www.youtube.com/embed/Szv4PxBgMLU"
                  frameBorder="0"
                  allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture"
                  allowFullScreen></iframe>
        </div>

      </div>
    </div>);
  }
}

export default PortalVideos;
