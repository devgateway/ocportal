import React from 'react';
import PropTypes from 'prop-types';

const GoBack = ({
  history, t, url, className,
}) => (
  <a href="#" onClick={() => (url ? history.push(url) : history.goBack())} className={`${className} back-link`}>
    <span className="back-icon">
      <span className="previous">&#8249;</span>
    </span>
    <span className="back-text">
      {t('general:goBack')}
    </span>
  </a>
);

GoBack.defaultProps = {
  className: '',
};

GoBack.propTypes = {
  history: PropTypes.shape({
    push: PropTypes.func.isRequired,
    goBack: PropTypes.func.isRequired,
  }).isRequired,
  t: PropTypes.func.isRequired,
  url: PropTypes.string,
  className: PropTypes.string,
};

export default GoBack;
