import React from 'react';
import ReactDOM from 'react-dom';
import { List } from 'immutable';
import { POPUP_HEIGHT } from '../constants';
import { getAwardAmount, mkContractLink, _3LineText } from '../tools';
import PaginatedTable from '../paginated-table';
import BootstrapTableWrapper from '../archive/bootstrap-table-wrapper';
import PropTypes from 'prop-types';

// eslint-disable-next-line no-undef
class Popup extends React.Component {
  constructor(...args) {
    super(...args);
    this.state = {
      showPopup: false,
    };
  }

  getPopup() {
    const { type, flagIds, t } = this.props;
    const { popupTop } = this.state;
    return (
      <div className="crd-popup text-center" style={{ top: popupTop, transform: 'none' }}>
        <div className="row">
          <div className="col-sm-12 info">
            <h5>
              {t('crd:procurementsTable:associatedFlags')
                .replace('$#$', t(`crd:corruptionType:${type}:name`))}
            </h5>
          </div>
          <div className="col-sm-12">
            <hr />
          </div>
          <div className="col-sm-12 info">
            {flagIds.map((flagId) => <p key={flagId}>{t(`crd:indicators:${flagId}:name`)}</p>)}
          </div>
        </div>
        <div className="arrow" />
      </div>
    );
  }

  showPopup() {
    const el = ReactDOM.findDOMNode(this);
    this.setState({
      showPopup: true,
      popupTop: -(POPUP_HEIGHT / 2) + (el.offsetHeight / 4),
    });
  }

  render() {
    const { flaggedStats, type } = this.props;
    const { showPopup } = this.state;
    let count = 0;
    if (flaggedStats.has('count')) {
      count = flaggedStats.get('count');
    } else {
      count = flaggedStats.find((stat) => stat.get('type') === type).get('count');
    }

    return (
      <div
        onMouseEnter={() => this.showPopup()}
        onMouseLeave={() => this.setState({ showPopup: false })}
      >
        {count}
        {showPopup && this.getPopup()}
      </div>
    );
  }
}

Popup.propTypes = {
  t: PropTypes.func.isRequired,
};

class ProcurementsTable extends PaginatedTable {
  renderPopup({ flaggedStats, flagType: type, flagIds }) {
    const { t } = this.props;
    return (
      <Popup
        flaggedStats={flaggedStats}
        type={type}
        flagIds={flagIds}
        t={t}
      />
    );
  }

  render() {
    const {
      data, navigate, corruptionType, t,
    } = this.props;

    if (!data) return null;

    const contracts = data.get('data', List());
    const count = data.getIn(['count', 0, 'count'], 0);

    const { pageSize, page } = this.state;

    const jsData = contracts.map((contract) => {
      const tenderAmount = `${contract.getIn(['tender', 'value', 'amount'], 'N/A')
      } ${
        contract.getIn(['tender', 'value', 'currency'], '')}`;

      const tenderPeriod = contract.get('tenderPeriod');
      const startDate = new Date(tenderPeriod.get('startDate')).toLocaleDateString();
      const endDate = new Date(tenderPeriod.get('endDate')).toLocaleDateString();

      const flags = contract.get('flags');
      const flaggedStats = flags.get('flaggedStats');
      const flagType = flaggedStats.get('type', corruptionType);
      const flagIds = flags
        .filter(
          (flag) => flag.has && flag.has('types') && flag.get('types').includes(flagType) && flag.get('value'),
        )
        .keySeq();

      return {
        status: contract.getIn(
          ['tender', 'status'],
          contract.get('status', 'N/A'),
        ),
        id: contract.get('ocid'),
        title: contract.get('title', 'N/A'),
        PEName: contract.getIn(['procuringEntity', 'name'], 'N/A'),
        tenderAmount,
        awardsAmount: getAwardAmount(contract),
        tenderDate: `${startDate}—${endDate}`,
        flagTypeName: t(`crd:corruptionType:${flagType}:name`),
        // needed for the popup:
        flaggedStats,
        flagType,
        flagIds,
      };
    }).toArray();

    return (
      <BootstrapTableWrapper
        data={jsData}
        count={count}
        page={page}
        onPageChange={(newPage) => this.setState({ page: newPage })}
        pageSize={pageSize}
        onSizePerPageList={(newPageSize) => this.setState({ pageSize: newPageSize })}
        columns={[
          {
            text: t('crd:procurementsTable:status'),
            dataField: 'status',
            fm: 'crd.flag.indicator.procurements.col.status',
          },
          {
            text: t('crd:procurementsTable:contractID'),
            dataField: 'id',
            fm: 'crd.flag.indicator.procurements.col.contractId',
            formatter: mkContractLink(navigate),
          },
          {
            text: t('crd:procurementsTable:title'),
            dataField: 'title',
            fm: 'crd.flag.indicator.procurements.col.title',
            formatter: mkContractLink(navigate),
          },
          {
            text: t('crd:procurementsTable:procuringEntity'),
            dataField: 'PEName',
            fm: 'crd.flag.indicator.procurements.col.procuringEntity',
            formatter: _3LineText,
          },
          {
            text: t('crd:procurementsTable:tenderAmount'),
            fm: 'crd.flag.indicator.procurements.col.tenderAmount',
            dataField: 'tenderAmount',
          },
          {
            text: t('crd:procurementsTable:awardsAmount'),
            fm: 'crd.flag.indicator.procurements.col.awardsAmount',
            dataField: 'awardsAmount',
          },
          {
            text: t('crd:procurementsTable:tenderDate'),
            fm: 'crd.flag.indicator.procurements.col.tenderDate',
            dataField: 'tenderDate',
          },
          {
            text: t('crd:procurementsTable:individualIndicator:noOfFlags')
              .replace('$#$', t(`crd:corruptionType:${corruptionType}:name`)),
            dataField: 'dummy1',
            fm: 'crd.flag.indicator.procurements.col.nrFlags',
            formatter: (_, popupData) => this.renderPopup(popupData),
            classes: 'hoverable popup-left',
          },
        ]}
      />
    );
  }
}

ProcurementsTable.propTypes = {
  t: PropTypes.func.isRequired,
};

export default ProcurementsTable;
