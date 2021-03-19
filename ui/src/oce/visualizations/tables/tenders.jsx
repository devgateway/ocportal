import Table from './index';
import fmConnect from '../../fm/fm';
import PropTypes from 'prop-types';

// safely gets the date from the data and formats it
const getDate = (obj/* tender obj */, key/* date key */) => (obj.hasIn(['tenderPeriod', key])
  ? new Date(obj.getIn(['tenderPeriod', key])).toLocaleDateString(undefined, Table.DATE_FORMAT)
  : '');

class Tenders extends Table {
  row(entry) {
    const bidNo = entry.getIn(['planning', 'bidNo']);
    const buyer = entry.get('buyer');
    const tender = entry.get('tender');
    const value = tender.get('value');

    // TODO - change the key when we have a bidNo
    return (
      <tr key={bidNo + tender}>
        {/* <td>{bidNo}</td> */}
        <td>{getDate(tender, 'startDate')}</td>
        <td>{getDate(tender, 'endDate')}</td>
        <td className="procuring-entity-title">{buyer && buyer.getIn(['name'])}</td>
        <td className="procuring-entity-title">{tender && tender.getIn(['title']) && tender.getIn(['title']).toUpperCase()}</td>
        <td>
          {this.maybeFormat(value && value.get('amount'))}
          {' '}
          {value && value.get('currency')}
        </td>
      </tr>
    );
  }

  render() {
    if (!this.props.data) return null;
    const { t } = this.props;
    return (
      <table className="table table-striped table-hover tenders-table">
        <thead>
          <tr>
            {/* <th>{t('tables:top10tenders:number')}</th> */}
            <th>{t('tables:top10tenders:startDate')}</th>
            <th>{t('tables:top10tenders:endDate')}</th>
            <th>{t('tables:top10tenders:buyer')}</th>
            <th>{t('tables:top10tenders:tenderTitle')}</th>
            <th>{t('tables:top10tenders:estimatedValue')}</th>
          </tr>
        </thead>
        <tbody>
          {this.props.data.map(this.row.bind(this))}
        </tbody>
      </table>
    );
  }
}

Tenders.getName = (t) => t('tables:top10tenders:title');
Tenders.endpoint = 'topTenLargestTenders';

Tenders.propTypes = {
  t: PropTypes.func.isRequired,
};

export default fmConnect(Tenders, 'viz.me.table.tenders');
