import {API_ROOT} from '../../../state/oce-state';
import {mtState} from '../state';
import React from 'react';
import {Item} from "./Item";
import fmConnect from "../../../fm/fm";
import FileDownloadLinks from "./FileDownloadLinks";
import translatable from "../../../translatable";

class Project extends translatable(React.Component) {
  constructor(props) {
    super(props);

    this.state = {};

    this.projectID = mtState.input({
      name: 'projectID',
    });

    this.projectInfoUrl = mtState.mapping({
      name: 'projectInfoUrl',
      deps: [this.projectID],
      mapper: id => `${API_ROOT}/makueni/project/id/${id}`,
    });

    this.projectInfo = mtState.remote({
      name: 'projectInfo',
      url: this.projectInfoUrl,
    });
  }

  componentDidMount() {
    const { id } = this.props;

    this.projectID.assign('Project', id);

    this.projectInfo.addListener('Project', () => {
      this.projectInfo.getState()
      .then(data => {
        this.setState({ data: data });
      });
    });
  }

  componentWillUnmount() {
    this.projectInfo.removeListener('Project');
  }

  getFeedbackSubject() {
    const { data } = this.state;

    let metadata;
    if (data !== undefined) {
      metadata = " - " + data.projects.projectTitle
        + " - " + data.department.label
        + " - " + data.fiscalYear.name;
    }
    return escape(this.t("project:feedbackSubject") + metadata);
  }

  render() {
    const { navigate, isFeatureVisible } = this.props;
    const { data } = this.state;

    const { currencyFormatter, formatDate } = this.props.styling.tables;

    return (<div className="project makueni-form">
      <div className="row">
        <a href="#!/tender" onClick={() => navigate()} className="back-link col-md-3">
        <span className="back-icon">
          <span className="previous">&#8249;</span>
        </span>
          <span className="back-text">
            {this.t("general:goBack")}
        </span>
        </a>
      </div>

      <div className="row padding-top-10">
        <div className="col-md-12">
          <h1 className="page-title">{this.t("project:pageTitle")}</h1>
        </div>
      </div>

      {
        data !== undefined
          ? <div>
            <div className="row display-flex">
              {isFeatureVisible("publicView.project.projectTitle")
              && <Item label={this.t("project:title")} value={data.projects.projectTitle} col={6} />}

              {isFeatureVisible("publicView.project.cabinetPapers")
              && <Item label={this.t("project:title")} col={6}>
                <FileDownloadLinks files={data.projects.cabinetPapers.flatMap(cp => cp.formDocs)} useDash />
              </Item>}

              {isFeatureVisible("publicView.project.amountBudgeted")
              && <Item label={this.t("project:amountBudgeted")} value={currencyFormatter(data.projects.amountBudgeted)} col={6} />}

              {isFeatureVisible("publicView.project.amountRequested")
              && <Item label={this.t("project:amountRequested")} value={currencyFormatter(data.projects.amountRequested)} col={6} />}

              {isFeatureVisible("publicView.project.subcounties")
              && <Item label={this.t("project:subcounties")} value={data.projects.subcounties.map(item => item.label).join(', ')} col={6} />}

              {isFeatureVisible("publicView.project.wards")
              && <Item label={this.t("project:wards")} value={data.projects.wards && data.projects.wards.map(item => item.label).join(', ')} col={6} />}

              {isFeatureVisible("publicView.project.approvedDate")
              && <Item label={this.t("project:approvedDate")} value={formatDate(data.projects.approvedDate)} col={6} />}
            </div>
          </div>
          : null
      }

    </div>);
  }

}

export default fmConnect(Project);
