import Header from '../../layout/header';
import BootstrapTableWrapper from '../../corruption-risk/archive/bootstrap-table-wrapper';

import '../makueni.scss';
import Project from './single/Project';
import PurchaseReqView from './single/PurchaseReqView';
import FiltersTendersWrapper from '../filters/FiltersTendersWrapper';
import Footer from '../../layout/footer';
import React, {useEffect, useState} from 'react';
import fmConnect from "../../fm/fm";
import FileDownloadLinks from "./single/FileDownloadLinks";
import { getTenders} from "../../api/Api";
import PropTypes from "prop-types";
import {tCreator} from "../../translatable";

const MakueniTenders = props =>  {

  useEffect(() => window.scrollTo(0, 0));

  const [filters, setFilters] = useState({});
  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(20);
  const [dataWithCount, setDataWithCount] = useState({data:[], count:undefined})

  useEffect(() => {
    getTenders({...filters, pageSize: pageSize, pageNumber: page - 1}).then(result => {
      setDataWithCount({
        data: result.data,
        count: result.count
      });
    });

  }, [filters, page, pageSize]);

  useEffect(() => {
    setPage(1);
  }, [filters]);

  let introJsCount = 0; //this should not be part of the state

  const filtersWrapper = () =>
      <FiltersTendersWrapper
          filters={filters} applyFilters={setFilters} translations={props.translations}/>;

  const t = tCreator(props.translations);

  const showDataStep = () => introJsCount++ < 6;

  const tenderLink = navigate => {
    return (tender) => (<div className="tender-title" data-step={showDataStep()?9:""}
    data-intro={showDataStep()?t("tables:tenderLink:dataIntro"):""}>
      {
        tender !== undefined
          ? <a href={`#!/tender/t/${tender.purchaseReqId}`}
               onClick={() => navigate('t', tender.purchaseReqId)} className="more-details-link">
            {tender.tenderTitle && tender.tenderTitle.toUpperCase()}
          </a> : t("tables:tenderLink:noTender")
      }
    </div>);
  }

  const projectLink = navigate => {
    return (project) => (<div data-step={showDataStep()?10:""}
                              data-intro={showDataStep()?t("tables:projectLink:dataIntro"):""}>
      {
        project !== undefined
          ? <a href={`#!/tender/p/${project._id}`} onClick={() => navigate('p', project._id)}
               className="more-details-link">
            {project.projectTitle && project.projectTitle.toUpperCase()}
          </a> : t("tables:projectLink:noProject")
      }
    </div>);
  }

  const downloadFiles = tender => {
    return <FileDownloadLinks files={(tender || {}).formDocs} />
  }

  const linksAndFiles = () => {
    const { isFeatureVisible } = props;
    return (tender) => <div data-step={showDataStep()?11:""}
                            data-intro={showDataStep()?"Click to download the tender document hardcopy.":""}>
      {isFeatureVisible('publicView.tendersProcessList.tenderDocs')
      && downloadFiles(tender)}

      {isFeatureVisible('publicView.tendersProcessList.tenderLink')
      && downloadLinks(tender)}
    </div>;
   }

   const downloadLinks = tender => {
    return tender && tender.tenderLink
      ? (<a className="download-file" href={tender.tenderLink} target="_blank">{tender.tenderLink}</a>)
      : null;
  }

    const { navigate, route, isFeatureVisible } = props;
    const [navigationPage, id] = route;

    const columns = [{
      title: t("tables:tenders:col:title"),
      dataField: 'tender',
      dataFormat: tenderLink(navigate),
      visible: isFeatureVisible('publicView.tendersProcessList.tenderTitle')
    }, {
      title: t("tables:tenders:col:department"),
      dataField: 'department',
      visible: isFeatureVisible('publicView.tendersProcessList.department')
    }, {
      title: t("tables:tenders:col:fiscalYear"),
      dataField: 'fiscalYear',
      visible: isFeatureVisible('publicView.tendersProcessList.fiscalYear')
    }, {
      title: t("tables:tenders:col:closingDate"),
      dataField: 'tender',
      dataFormat: (cell, row) => cell !== undefined ? new Date(cell.closingDate).toLocaleDateString() : '',
      visible: isFeatureVisible('publicView.tendersProcessList.closingDate')
    }, {
      title: t("tables:tenders:col:tenderValue"),
      dataField: 'tender',
      dataFormat: (cell, row) => cell !== undefined ? props.styling.charts.hoverFormatter(cell.tenderValue) : '',
      visible: isFeatureVisible('publicView.tendersProcessList.tenderValue')
    }, {
      title: t("tables:tenders:col:project"),
      dataField: 'project',
      dataFormat: projectLink(navigate),
      visible: isFeatureVisible('publicView.tendersProcessList.project')
    }, {
      title: t("tables:tenders:col:documents"),
      dataField: 'tender',
      dataFormat: linksAndFiles(),
      visible: isFeatureVisible('publicView.tendersProcessList.tenderLink')
        || isFeatureVisible('publicView.tendersProcessList.tenderDocs')
    }];

  const visibleColumns = columns.filter(c => c.visible);

    return (<div className="container-fluid dashboard-default">

      <Header translations={props.translations} onSwitch={props.onSwitch}
              styling={props.styling} selected="tender"/>

      <div className="makueni-tenders content row" >
        <div className="col-md-3 col-sm-12 filters">
          <div className="row" data-intro={t("tables:tenders:filters:dataIntro")} data-step="8">
            <div className="filters-hint col-md-12">
              {t('filters:hint')}
            </div>
            {filtersWrapper()}
          </div>
        </div>

        <div className="col-md-9 col-sm-12 col-main-content">
          {
            navigationPage === undefined
              ? <div>
                <h1>{t("tables:tenders:title")}</h1>

                <BootstrapTableWrapper
                  bordered
                  data={dataWithCount.data}
                  page={page}
                  pageSize={pageSize}
                  onPageChange={setPage}
                  onSizePerPageList={setPageSize}
                  count={dataWithCount.count}
                  columns={visibleColumns}
                />
              </div>
              : navigationPage === 't'
              ? <PurchaseReqView id={id} navigate={navigate}
                                 onSwitch={props.onSwitch}
                                 translations={props.translations}
                                 styling={props.styling}/>
              : <Project id={id} navigate={navigate} translations={props.translations}
                         styling={props.styling}/>
          }
        </div>
      </div>

      <div className="alerts-container">
        <div className="row alerts-button subscribe">
          <div className="col-md-12">
            <button className="btn btn-info btn-lg" type="submit"
                    onClick={() => props.onSwitch('alerts')}>{t("general:subscribeToEmailAlerts")}
            </button>
          </div>
        </div>
      </div>
      <div className="smshelp-container">
        <div className="row alerts-button subscribe">
          <div className="col-md-12">
            <button className="btn btn-info btn-lg" type="submit"
                    onClick={() => props.onSwitch('smshelp')}>{t("general:smsFeedbackHelp")}
            </button>
          </div>
        </div>
      </div>
      <Footer translations={props.translations}/>
    </div>);
}

MakueniTenders.propTypes = {
  styling: PropTypes.object.isRequired
};

export default fmConnect(MakueniTenders);
