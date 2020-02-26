import Table from './index';
import orgnamesFetching from '../../orgnames-fetching';
import { pluckImm } from '../../tools';

class TopSuppliersInspectionReportNoPay extends orgnamesFetching(Table) {
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
      <td>{entry.get('count')}</td>
    </tr>;
  }

  render() {
    if (!this.props.data) return null;
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

TopSuppliersInspectionReportNoPay.getName = t => t('tables:topSuppliersInspectionNoPay:title');
TopSuppliersInspectionReportNoPay.endpoint = 'topSuppliersInspectionNoPay';

export default TopSuppliersInspectionReportNoPay;
