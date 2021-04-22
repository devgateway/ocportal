import React from 'react';
import PropTypes from 'prop-types';
import { useHistory } from 'react-router-dom';

const GoBack = ({
  t, url,
}) => {
  const history = useHistory();
  return (
    <button
      type="button"
      onClick={() => (url ? history.push(url) : history.goBack())}
      className="btn btn-link back-link"
    >
      <span className="glyphicon glyphicon-menu-left" aria-hidden="true" />
      {t('general:goBack')}
    </button>
  );
};

GoBack.propTypes = {
  t: PropTypes.func.isRequired,
  url: PropTypes.string,
};

export default GoBack;
