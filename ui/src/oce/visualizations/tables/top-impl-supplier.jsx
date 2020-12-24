import React from 'react';
import Table from './index';
import orgnamesFetching from '../../orgnames-fetching';
import { pluckImm } from '../../tools';
import ContractsList from '../../makueni/ContractsList';

class TopImplSupplier extends orgnamesFetching(Table) {
  constructor(...args) {
    super(...args);
    this.state = this.state || {};
    this.state.contractorId = null;
  }

  getOrgsWithoutNamesIds() {
    if (!this.props.data) return [];
    return this.props.data.map(pluckImm('_id'))
      .filter((id) => !this.state.orgNames[id])
      .toJS();
  }

  row(entry) {
    const id = entry.get('_id');
    return (
      <tr key={id}>
        <td>{this.getOrgName(id)}</td>
        <td>
          <a onClick={(e) => this.setContractorId(id)} className="more-details-link">
            {entry.get('count')}
          </a>
        </td>
      </tr>
    );
  }

  setContractorId(id) {
    this.setState({ contractorId: id });
  }

  resetContractorID() {
    this.setState({ contractorId: null });
  }

  render() {
    const { contractorId } = this.state;
    const { contractListEndpoint } = this.constructor;
    if (!this.props.data) return null;
    if (contractorId) {
      return (
        <ContractsList
          contractorId={contractorId}
          endpointName={contractListEndpoint}
          resetContractorID={this.resetContractorID.bind(this)}
        />
      );
    }
    return (
      <table className="table table-striped table-hover suppliers-table">
        <thead>
          <tr>
            <th>{this.t('tables:topImplSupplier:supplierName')}</th>
            <th>{this.t('tables:topImplSupplier:noNotPay')}</th>
          </tr>
        </thead>
        <tbody>
          {this.props.data.map((entry) => this.row(entry))}
        </tbody>
      </table>
    );
  }
}

export default TopImplSupplier;
