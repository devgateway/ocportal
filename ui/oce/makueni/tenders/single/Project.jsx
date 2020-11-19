import {API_ROOT} from '../../../state/oce-state';
import {mtState} from '../state';
import React from 'react';
import {Item} from "./Item";
import fmConnect from "../../../fm/fm";
import FileDownloadLinks from "./FileDownloadLinks";

class Project extends React.Component {
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
    return escape("Project" + metadata);
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
          Go Back
        </span>
        </a>
      </div>

      <div className="row padding-top-10">
        <div className="col-md-12">
          <h1 className="page-title">Project</h1>
        </div>
      </div>

      {
        data !== undefined
          ? <div>
            <div className="row">
              {isFeatureVisible("projectForm.projectTitle")
              && <Item label="Project Title" value={data.projects.projectTitle} col={6} />}

              {isFeatureVisible("projectForm.cabinetPapers")
              && <Item label="Cabinet Papers" col={6}>
                <FileDownloadLinks files={data.projects.cabinetPapers.flatMap(cp => cp.formDocs)} />
              </Item>}
            </div>

            <div className="row">
              {isFeatureVisible("projectForm.amountBudgeted")
              && <Item label="Amount Budgeted" value={currencyFormatter(data.projects.amountBudgeted)} col={6} />}

              {isFeatureVisible("projectForm.amountRequested")
              && <Item label="Amount Requested" value={currencyFormatter(data.projects.amountRequested)} col={6} />}
            </div>

            <div className="row">
              {isFeatureVisible("projectForm.subcounties")
              && <Item label="Sub-Counties" value={data.projects.subcounties.map(item => item.label).join(', ')} col={6} />}

              {isFeatureVisible("projectForm.wards")
              && <Item label="Wards" value={data.projects.wards && data.projects.wards.map(item => item.label).join(', ')} col={6} />}
            </div>

            <div className="row">
              {isFeatureVisible("projectForm.approvedDate")
              && <Item label="Approved Date" value={formatDate(data.projects.approvedDate)} col={6} />}
            </div>
          </div>
          : null
      }

    </div>);
  }

}

export default fmConnect(Project);
