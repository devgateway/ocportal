import Table from './index';
import orgnamesFetching from '../../orgnames-fetching';
import { pluckImm } from '../../tools';
import React from 'react';
import ContractsList from '../../makueni/ContractsList';

class TopSuppliersInspectionReportNoPay extends orgnamesFetching(Table) {

  constructor(...args) {
    super(...args);
    this.state = this.state || {};
    this.state.contractorId = null;
  }

  getOrgsWithoutNamesIds() {
    if (!this.props.data) return [];
    return this.props.data.map(pluckImm('_id'))
      .filter(id => !this.state.orgNames[id])
      .toJS();
  }

  row(entry) {
    const id = entry.get('_id');
    return <tr key={id}>
      <td>{this.getOrgName(id)}</td>
      <td>
        <a href="javascript:void(0);"
           onClick={e => this.setContractorId(id)} className="more-details-link">
          {entry.get('count')}</a>
      </td>
    </tr>;
  }

  setContractorId(id) {
    this.setState({ contractorId: id });
  }

  resetContractorID() {
    this.setState({ contractorId: null });
  }

  render() {
    const { contractorId } = this.state;
    if (!this.props.data) return null;
    if (contractorId) {
      return (<ContractsList contractorId={contractorId}
                             resetContractorID={this.resetContractorID.bind(this)}/>);
    } else {
      return (
        <table className="table table-striped table-hover suppliers-table">
          <thead>
          <tr>
            <th>{this.t('tables:topSuppliersInspectionNoPay:supplierName')}</th>
            <th>{this.t('tables:topSuppliersInspectionNoPay:noNotPay')}</th>
          </tr>
          </thead>
          <tbody>
          {this.props.data.map(entry => this.row(entry))}
          </tbody>
        </table>
      );
    }
  }
}

TopSuppliersInspectionReportNoPay.getName = t => t('tables:topSuppliersInspectionNoPay:title');
TopSuppliersInspectionReportNoPay.endpoint = 'topSuppliersInspectionNoPay';

export default TopSuppliersInspectionReportNoPay;
