import React from 'react';
import { useTranslation } from 'react-i18next';

const NoDataMessage = () => {
  const { t } = useTranslation();
  return (
    <div className="no-data-wrapper">
      <h2>{t('general:noData')}</h2>
    </div>
  );
};

export default NoDataMessage;
