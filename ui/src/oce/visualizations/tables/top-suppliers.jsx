import Table from './index';
import { pluckImm } from '../../tools';
import orgnamesFetching from '../../orgnames-fetching';
import fmConnect from '../../fm/fm';
import PropTypes from 'prop-types';

class Suppliers extends orgnamesFetching(Table) {
  getOrgsWithoutNamesIds() {
    if (!this.props.data) return [];
    return this.props.data.map(pluckImm('supplierId'))
      .filter((id) => !this.state.orgNames[id])
      .toJS();
  }

  row(entry) {
    const id = entry.get('supplierId');
    return (
      <tr key={id}>
        <td>{this.getOrgName(id)}</td>
        <td>{entry.get('totalContracts')}</td>
        <td>{entry.get('buyerIdsCount')}</td>
        <td>{this.maybeFormat(entry.get('totalAwardAmount'))}</td>
      </tr>
    );
  }

  render() {
    if (!this.props.data) return null;
    const { t } = this.props;
    return (
      <table className="table table-striped table-hover suppliers-table">
        <thead>
          <tr>
            <th>{t('tables:top10suppliers:supplierName')}</th>
            <th>{t('tables:top10suppliers:nrAwardsWon')}</th>
            <th>{t('tables:top10suppliers:nrPE')}</th>
            <th>{t('tables:top10suppliers:totalAwardedValue')}</th>
          </tr>
        </thead>
        <tbody>
          {this.props.data.map((entry) => this.row(entry))}
        </tbody>
      </table>
    );
  }
}

Suppliers.getName = (t) => t('tables:top10suppliers:title');
Suppliers.endpoint = 'topTenSuppliers';

Suppliers.propTypes = {
  t: PropTypes.func.isRequired,
};

export default fmConnect(Suppliers, 'viz.me.table.suppliers');
