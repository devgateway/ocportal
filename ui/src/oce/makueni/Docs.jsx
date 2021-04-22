import React from 'react';
import { Link } from 'react-router-dom';
import CRDPage from '../corruption-risk/page';
import Header from '../layout/header';
import './makueni.scss';
import Footer from '../layout/footer';

class Docs extends CRDPage {
  render() {
    const { t } = this.props;
    return (
      <div className="container-fluid dashboard-default">
        <Header
          styling={this.props.styling}
          selected="docs"
        />
        <div className="makueni-procurement-plan content row">

          <div className="col-md-2 col-md-offset-5">
            <h1>{t('docs:resources')}</h1>
          </div>

          <div className="col-md-6 col-md-offset-3">
            <div className="list-group">
              <Link to="/portal/portal-videos" className="list-group-item">
                <h3 className="list-group-item-heading">{t('docs:portalVideos:heading')}</h3>
              </Link>
              <a href="/swagger-ui/" className="list-group-item">
                <h3 className="list-group-item-heading">{t('docs:allEndpoints:heading')}</h3>
                <p className="list-group-item-text">{t('docs:allEndpoints:text')}</p>
              </a>
              <a href="/swagger-ui/#/makueni-data-controller" className="list-group-item">
                <h3 className="list-group-item-heading">{t('docs:makueniEndpoints:heading')}</h3>
                <p className="list-group-item-text">{t('docs:makueniEndpoints:text')}</p>
              </a>
              <a href="/swagger-ui/#/ocds-controller" className="list-group-item">
                <h3 className="list-group-item-heading">{t('docs:ocdsEndpoints:heading')}</h3>
                <p className="list-group-item-text">{t('docs:ocdsEndpoints:text')}</p>
              </a>
              <a href="https://standard.open-contracting.org/1.1/en/" className="list-group-item">
                <h3 className="list-group-item-heading">{t('docs:ocdsDocs:heading')}</h3>
                <p className="list-group-item-text">{t('docs:ocdsDocs:text')}</p>
              </a>
              <a href="https://github.com/devgateway/forms-makueni" className="list-group-item">
                <h3 className="list-group-item-heading">{t('docs:sourceCode:heading')}</h3>
                <p className="list-group-item-text">{t('docs:sourceCode:text')}</p>
              </a>
              <a href="https://github.com/devgateway/forms-makueni/blob/develop/LICENSE" className="list-group-item">
                <h3 className="list-group-item-heading">{t('docs:license:heading')}</h3>
                <p className="list-group-item-text">{t('docs:license:text')}</p>
              </a>
              <Link to="/portal/publication-policy" className="list-group-item">
                <h3 className="list-group-item-heading">{t('docs:policy:heading')}</h3>
                <p className="list-group-item-text">{t('docs:policy:text')}</p>
              </Link>
            </div>
          </div>
        </div>
        <Footer />
      </div>
    );
  }
}

export default Docs;
