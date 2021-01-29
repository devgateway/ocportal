import React from 'react';
import PropTypes from 'prop-types';
import { useTranslation } from 'react-i18next';
import { Item } from './Item';
import ImplReport from './ImplReport';
import defaultSingleTenderTabTypes from './singleUtil';

const AuthImplReport = (props) => {
  const { authChildren, fmPrefix, childElements } = props;
  const { t } = useTranslation();

  const authChildElements = (i) => {
    const { formatBoolean } = props.styling.tables;
    const { isFeatureVisible } = props;
    return ([childElements && childElements(i), <div key="1" className="row">
      {isFeatureVisible(`${fmPrefix}.authorizePayment`)
      && <Item label={t('authImplReport:authorizePayment')} value={formatBoolean(i.authorizePayment)} col={3} />}
      {
        authChildren && authChildren(i)
      }
    </div>]);
  };

  return (<ImplReport {...props} childElements={authChildElements} />);
};

AuthImplReport.propTypes = {
  ...defaultSingleTenderTabTypes,
  childElements: PropTypes.func,
  reportName: PropTypes.string.isRequired,
  fmPrefix: PropTypes.string.isRequired,
  authChildren: PropTypes.func,
};

export default AuthImplReport;
