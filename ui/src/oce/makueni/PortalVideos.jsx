import CRDPage from '../corruption-risk/page';
import Header from '../layout/header';
import './makueni.scss';
import React from 'react';

class PortalVideos extends CRDPage {
  render() {
    const { t } = this.props;
    return (
      <div className="container-fluid dashboard-default">
        <Header
          styling={this.props.styling}
          selected="docs"
        />
        <div className="makueni-procurement-plan content row">

          <div className="col-md-4 col-md-offset-5">
            <h1>{t('portalVideos:title')}</h1>
          </div>

          <div className="col-md-8 col-md-offset-4 text-justify">
            <h2>{t('portalVideos:video1')}</h2>
            <iframe
              width="640"
              height="480"
              src="https://www.youtube.com/embed/ER9wfYw1dnw"
              frameBorder="0"
              allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture"
              allowFullScreen
            />
          </div>

          <div className="col-md-8 col-md-offset-4 text-justify">
            <h2>{t('portalVideos:video2')}</h2>
            <iframe
              width="640"
              height="480"
              src="https://www.youtube.com/embed/bI4Yt_CKR_A"
              frameBorder="0"
              allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture"
              allowFullScreen
            />
          </div>

          <div className="col-md-8 col-md-offset-4 text-justify">
            <h2>{t('portalVideos:video3')}</h2>
            <iframe
              width="640"
              height="480"
              src="https://www.youtube.com/embed/wBiy17jijYo"
              frameBorder="0"
              allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture"
              allowFullScreen
            />
          </div>

          <div className="col-md-8 col-md-offset-4 text-justify">
            <h2>{t('portalVideos:video4')}</h2>
            <iframe
              width="640"
              height="480"
              src="https://www.youtube.com/embed/7D1V9kqzQ-M"
              frameBorder="0"
              allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture"
              allowFullScreen
            />
          </div>

          <div className="col-md-8 col-md-offset-4 text-justify">
            <h2>{t('portalVideos:video5')}</h2>
            <iframe
              width="640"
              height="480"
              src="https://www.youtube.com/embed/Szv4PxBgMLU"
              frameBorder="0"
              allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture"
              allowFullScreen
            />
          </div>

        </div>
      </div>
    );
  }
}

export default PortalVideos;
