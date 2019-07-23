import CRDPage from '../../../corruption-risk/page';
import { ppState } from '../state';
import { API_ROOT } from '../../../state/oce-state';
import { OverlayTrigger, Tooltip } from 'react-bootstrap';

class ProcurementPlan extends CRDPage {
  constructor(props) {
    super(props);
    
    this.state = {};
    
    this.ppID = ppState.input({
      name: 'ppID',
    });
    
    this.ppInfoUrl = ppState.mapping({
      name: 'ppInfoUrl',
      deps: [this.ppID],
      mapper: id => `${API_ROOT}/makueni/procurementPlan/id/${id}`,
    });
    
    this.ppInfo = ppState.remote({
      name: 'ppInfo',
      url: this.ppInfoUrl,
    });
  }
  
  componentDidMount() {
    super.componentDidMount();
    
    const { id } = this.props;
    
    this.ppID.assign('PP', id);
    
    this.ppInfo.addListener('PP', () => {
      this.ppInfo.getState()
      .then(data => {
        this.setState({ data: data });
      });
    });
  }
  
  componentWillUnmount() {
    this.ppInfo.removeListener('PP');
  }
  
  render() {
    const { navigate } = this.props;
    const { data } = this.state;
    
    return (<div className="procurement-plan makueni-form">
      <div className="row">
        <a href="#!/procurement-plan" onClick={() => navigate()} className="back-link col-md-3">
        <span className="back-icon">
          <span className="previous">&#8249;</span>
        </span>
          <span>
          Go Back
        </span>
        </a>
      </div>
      
      <div className="row padding-top-10">
        <div className="col-md-12">
          <h1 className="page-title">Procurement Plan</h1>
        </div>
      </div>
      
      {
        data !== undefined
          ? <div>
            <div className="row">
              <div className="col-md-8">
                <div className="item-label">Department</div>
                <div className="item-value">{data.department.label}</div>
              </div>
              <div className="col-md-4">
                <div className="item-label">Fiscal Year</div>
                <div className="item-value">{data.fiscalYear.name}</div>
              </div>
            </div>
            
            {
              data.planItems !== undefined
                ? <div>
                  <div className="row padding-top-10">
                    <div className="col-md-12 sub-title">Procurement Plan Item
                      ({data.planItems.length})
                    </div>
                  </div>
                  
                  {
                    data.planItems.map(planItem => <div key={planItem.id} className="box">
                      <div className="row">
                        <div className="col-md-6">
                          <div className="item-label">Item</div>
                          <div className="item-value">{planItem.item.label}</div>
                        </div>
                        <div className="col-md-6">
                          <div className="item-label">Description</div>
                          <div className="item-value">{planItem.description}</div>
                        </div>
                      </div>
                      
                      
                      <div className="row padding-top-10">
                        <div className="col-md-3">
                          <div className="item-label">Estimated Cost</div>
                          <div className="item-value">{planItem.estimatedCost}</div>
                        </div>
                        <div className="col-md-2">
                          <div className="item-label">Unit Of Issue</div>
                          <div className="item-value">{planItem.unitOfIssue}</div>
                        </div>
                        <div className="col-md-2">
                          <div className="item-label">Quantity</div>
                          <div className="item-value">{planItem.quantity}</div>
                        </div>
                        <div className="col-md-2">
                          <div className="item-label">Unit Price</div>
                          <div className="item-value">{planItem.unitPrice}</div>
                        </div>
                        <div className="col-md-3">
                          <div className="item-label">Total Cost</div>
                          <div className="item-value">{planItem.totalCost}</div>
                        </div>
                      </div>
                      
                      <div className="row padding-top-10">
                        <div className="col-md-3">
                          <div className="item-label">Procurement Method</div>
                          <div className="item-value">{planItem.procurementMethod.label}</div>
                        </div>
                        <div className="col-md-3">
                          <div className="item-label">Source of Funds</div>
                          <div className="item-value">{planItem.sourceOfFunds}</div>
                        </div>
                        {
                          planItem.targetGroup !== undefined
                            ? <div className="col-md-3">
                              <div className="item-label">Target Group</div>
                              <div className="item-value">{planItem.targetGroup.label}</div>
                            </div>
                            : null
                        }
                        <div className="col-md-3">
                          <div className="item-label">
                            Target Group Value
                          </div>
                          <div className="item-value">{planItem.targetGroupValue}</div>
                        </div>
                      </div>
                      
                      <div className="row padding-top-10">
                        <div className="col-md-3">
                          <div className="item-label">1st Quarter</div>
                          <div className="item-value">{planItem.quarter1st}</div>
                        </div>
                        <div className="col-md-3">
                          <div className="item-label">2nd Quarter</div>
                          <div className="item-value">{planItem.quarter2nd}</div>
                        </div>
                        <div className="col-md-3">
                          <div className="item-label">3rd Quarter</div>
                          <div className="item-value">{planItem.quarter3rd}</div>
                        </div>
                        <div className="col-md-3">
                          <div className="item-label">4th Quarter</div>
                          <div className="item-value">{planItem.quarter4th}</div>
                        </div>
                      </div>
                    </div>)
                  }
                  
                  <div className="row padding-top-10">
                    <div className="col-md-6">
                      <div className="item-label">Procurement Plan Documents</div>
                      
                      {
                        data.formDocs.map(doc => <div key={doc.id}>
                          <OverlayTrigger
                            placement="bottom"
                            overlay={
                              <Tooltip id="download-tooltip">
                                Click to download the file
                              </Tooltip>
                            }>
  
                            <a className="item-value download" href={doc.url} target="_blank">
                              <i className="glyphicon glyphicon-download"/>
                              <span>{doc.name}</span>
                            </a>
                          </OverlayTrigger>
                        </div>)
                      }
                    </div>
                    <div className="col-md-6">
                      <div className="item-label">Approved Date</div>
                      <div
                        className="item-value">{new Date(data.approvedDate).toLocaleDateString()}</div>
                    </div>
                  </div>
                </div>
                : null
            }
          </div>
          : null
      }
    </div>);
  }
}

export default ProcurementPlan;
