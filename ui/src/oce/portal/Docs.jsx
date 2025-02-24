import React from 'react';
import { Link } from 'react-router-dom';
import CRDPage from '../corruption-risk/page';
import Header from '../layout/header';
import './portal.scss';
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
          <div className="client-procurement-plan content row justify-content-center">

            {/* Center the header text */}
            <div className="col-md-6 text-center">
              <h1 className="text-dark">{t('docs:resources')}</h1>
            </div>

            {/* Center the content and ensure proper text alignment */}
            <div className="col-md-8 text-center">
              <div className="list-group">
                <Link to="/portal/portal-videos" className="list-group-item">
                  <h3 className="text-dark">{t('docs:portalVideos:heading')}</h3>
                </Link>
                <a href="/swagger-ui/index.html" className="list-group-item">
                  <h3 className="text-dark">{t('docs:allEndpoints:heading')}</h3>
                  <p className="text-dark">{t('docs:allEndpoints:text')}</p>
                </a>
                <a href="/swagger-ui/index.html#/client-data-controller" className="list-group-item">
                  <h3 className="text-dark">{t('docs:makueniEndpoints:heading')}</h3>
                  <p className="text-dark">{t('docs:makueniEndpoints:text')}</p>
                </a>
                <a href="/swagger-ui/index.html#/ocds-controller" className="list-group-item">
                  <h3 className="text-dark">{t('docs:ocdsEndpoints:heading')}</h3>
                  <p className="text-dark">{t('docs:ocdsEndpoints:text')}</p>
                </a>
                <a href="https://standard.open-contracting.org/1.1/en/" className="list-group-item">
                  <h3 className="text-dark">{t('docs:ocdsDocs:heading')}</h3>
                  <p className="text-dark">{t('docs:ocdsDocs:text')}</p>
                </a>
                <a href="https://github.com/devgateway/forms-makueni" className="list-group-item">
                  <h3 className="text-dark">{t('docs:sourceCode:heading')}</h3>
                  <p className="text-dark">{t('docs:sourceCode:text')}</p>
                </a>
                <a href="https://github.com/devgateway/forms-makueni/blob/develop/LICENSE" className="list-group-item">
                  <h3 className="text-dark">{t('docs:license:heading')}</h3>
                  <p className="text-dark">{t('docs:license:text')}</p>
                </a>
                <Link to="/portal/publication-policy" className="list-group-item">
                  <h3 className="text-dark">{t('docs:policy:heading')}</h3>
                  <p className="text-dark">{t('docs:policy:text')}</p>
                </Link>
                <Link to="/portal/privacy-policy" className="list-group-item">
                  <h3 className="text-dark">{t('docs:privacyPolicy:heading')}</h3>
                  <p className="text-dark">{t('docs:privacyPolicy:text')}</p>
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
