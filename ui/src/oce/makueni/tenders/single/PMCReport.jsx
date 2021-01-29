import React from 'react';
import { useTranslation } from 'react-i18next';
import fmConnect from '../../../fm/fm';
import { Item } from './Item';
import AuthImplReport from './AuthImplReport';
import defaultSingleTenderTabTypes from './singleUtil';

const PMCReport = (props) => {
  const { t } = useTranslation();

  const authChildren = (i) => {
    const { isFeatureVisible } = props;
    return (
      <div>
        {isFeatureVisible('publicView.pmcReport.subcounties')
          && (
          <Item
            label={t('pmcReport:subcounties')}
            value={i.subcounties.map((item) => item.label).join(', ')}
            col={3}
          />
          )}

        {isFeatureVisible('publicView.pmcReport.wards')
          && <Item label={t('pmcReport:wards')} value={i.wards.map((item) => item.label).join(', ')} col={3} />}

        {isFeatureVisible('publicView.pmcReport.pmcStatus')
          && <Item label={t('pmcReport:pmcStatus')} value={i.pmcStatus.label} col={3} />}
      </div>
    );
  };

  const childElements = (i) => {
    const { isFeatureVisible } = props;
    return (
      <div key="2">
        <div className="row padding-top-10">
          {isFeatureVisible('publicView.pmcReport.projectClosureHandover')
        && (
        <Item
          label={t('pmcReport:projectClosureAndHandover')}
          value={i.projectClosureHandover && i.projectClosureHandover.map((item) => item.label).join(', ')}
          col={3}
        />
        )}
        </div>
        {isFeatureVisible('publicView.pmcReport.pmcMembers')
        && (
        <div>
          <div className="row">
            <div className="col-md-10">
              <div className="item-label">{t('pmcReport:members')}</div>
            </div>
          </div>
          {
                i.pmcMembers && i.pmcMembers.map((m) => (
                  <div key={m._id} className="row">
                    {isFeatureVisible('publicView.pmcReport.pmcMembers.staff')
                      && <Item label={t('pmcReport:staff')} value={m.staff.label} col={3} />}
                    {isFeatureVisible('publicView.pmcReport.pmcMembers.designation')
                      && <Item label={t('pmcReport:designation')} value={m.designation.label} col={3} />}
                  </div>
                ))
              }
        </div>
        )}
        <div className="row">
          {isFeatureVisible('publicView.pmcReport.socialSafeguards')
          && <Item label={t('pmcReport:socialSafeguards')} value={i.socialSafeguards} col={3} />}

          {isFeatureVisible('publicView.pmcReport.emergingComplaints')
          && <Item label={t('pmcReport:emergingComplaints')} value={i.emergingComplaints} col={3} />}

          {isFeatureVisible('publicView.pmcReport.pmcChallenges')
          && <Item label={t('pmcReport:pmcChallenges')} value={i.pmcChallenges} col={3} />}
        </div>
        {isFeatureVisible('publicView.pmcReport.pmcNotes')
        && (
        <div className="row">
          <Item
            label={t('pmcReport:pmcNotes')}
            value={i.pmcNotes && i.pmcNotes.map((item) => item.text).join(', ')}
            col={12}
          />
        </div>
        )}
      </div>
    );
  };

  return (
    <AuthImplReport
      {...props}
      reportName={t('pmcReport:label')}
      fmPrefix="publicView.pmcReport"
      authChildren={authChildren}
      childElements={childElements}
    />
  );
};

PMCReport.propTypes = defaultSingleTenderTabTypes;

export default fmConnect(PMCReport);
