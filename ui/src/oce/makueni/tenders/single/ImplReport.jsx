import NoDataMessage from './NoData';
import React from 'react';
import {Item} from "./Item";
import FileDownloadLinks from "./FileDownloadLinks";
import {tCreator} from "../../../translatable";
import PropTypes from "prop-types";

const ImplReport = (props) => {
  const {department, reportName, fmPrefix, childElements} = props;
  const {fiscalYear, data, tenderTitle, isFeatureVisible} = props;
  const t = tCreator(props.translations);
  const {formatDate} = props.styling.tables;

  const getFeedbackSubject = () => {
    let metadata;
    if (department !== undefined) {
      metadata = ' - ' + tenderTitle
          + ' - ' + department.label
          + ' - ' + fiscalYear.name;
    }
    return escape(reportName + metadata);
  }

  const getImplReport = () => {
    return (<div>
      <div className="padding-top-10">
        {
          data.sort((a, b) => new Date(a.approvedDate) - new Date(b.approvedDate))
              .map(i => <div key={i._id} className="box">
                <div className="row">
                  {isFeatureVisible(fmPrefix + ".tenderTitle")
                  && <Item label={t("report:tenderTitle")} value={tenderTitle} col={3}/>}

                  {isFeatureVisible(fmPrefix + ".contractor")
                  && <Item label={t("report:awardee:label")} value={i.contract.awardee.label} col={3}/>}

                  {isFeatureVisible(fmPrefix + ".fiscalYear")
                  && <Item label={t("report:fiscalYear")} value={fiscalYear.name} col={3}/>}

                  {isFeatureVisible(fmPrefix + ".approvedDate")
                  && <Item label={t("report:approvedDate")} value={formatDate(i.approvedDate)} col={3}/>}
                </div>
                {
                  childElements(i)
                }
                {
                  i.formDocs && isFeatureVisible(fmPrefix + ".formDocs") ?
                      <div className="row">
                        <Item label={t("report:uploads").replace("$#$", reportName)} col={12}>
                          <FileDownloadLinks files={i.formDocs} useDash/>
                        </Item>
                      </div>
                      : null
                }
              </div>)
        }
      </div>
    </div>);
  }

  return data === undefined ? <NoDataMessage translations={props.translations}/> : getImplReport();
}

ImplReport.propTypes = {
  styling: PropTypes.object.isRequired,
  fiscalYear: PropTypes.object.isRequired,
  department: PropTypes.object.isRequired,
  reportName: PropTypes.string.isRequired,
  fmPrefix: PropTypes.string.isRequired,
  childElements: PropTypes.func.isRequired,
  translations: PropTypes.object.isRequired,
  data: PropTypes.array,
  isFeatureVisible: PropTypes.func.isRequired,
  tenderTitle: PropTypes.string.isRequired
};

export default ImplReport;
