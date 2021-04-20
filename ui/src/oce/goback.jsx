import React from 'react';
import PropTypes from 'prop-types';

const GoBack = ({ history, t, url }) => (
  <div className="row">
    <a href="#" onClick={() => (url ? history.push(url) : history.goBack())} className="back-link col-md-3">
      <span className="back-icon">
        <span className="previous">&#8249;</span>
      </span>
      <span className="back-text">
        {t('general:goBack')}
      </span>
    </a>
  </div>
);

GoBack.propTypes = {
  history: PropTypes.func.isRequired,
  t: PropTypes.func.isRequired,
  url: PropTypes.string,
};

export default GoBack;
