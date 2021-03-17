import React from 'react';
import { useTranslation } from 'react-i18next';
import AuthImplReport from './AuthImplReport';
import fmConnect from '../../../fm/fm';
import defaultSingleTenderTabTypes from './singleUtil';

const AdministratorReport = (props) => {
  const { t } = useTranslation();

  return (
    <AuthImplReport
      {...props}
      reportName={t('administratorReport:reportName')}
      fmPrefix="publicView.administratorReport"
    />
  );
};

AdministratorReport.propTypes = defaultSingleTenderTabTypes;

export default fmConnect(AdministratorReport);
