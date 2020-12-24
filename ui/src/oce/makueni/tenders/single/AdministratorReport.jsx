import React from 'react';
import AuthImplReport from './AuthImplReport';
import { tCreator } from '../../../translatable';
import fmConnect from '../../../fm/fm';
import defaultSingleTenderTabTypes from './singleUtil';

const AdministratorReport = (props) => {
  const t = tCreator(props.translations);

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
