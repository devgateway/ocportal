import CenterTextDonut from './index';
import PropTypes from 'prop-types';

class NrOfBidders extends CenterTextDonut {
  getClassnames() {
    return super.getClassnames().concat('nr-of-bidders');
  }

  getCenterText() {
    const { count, data: avg } = this.props;
    if (avg == null || Number.isNaN(avg) || count == null || Number.isNaN(count)) return '';
    return (
      <div>
        {count}
        <span className="secondary">
          /
          {avg.toFixed(2)}
        </span>
      </div>
    );
  }

  getTitle() {
    const { t } = this.props;
    return t('crd:contract:nrBiddersVsAvg:name');
  }
}

NrOfBidders.propTypes = {
  t: PropTypes.func.isRequired,
};

NrOfBidders.Donut = class extends CenterTextDonut.Donut {
  transform(data) {
    try {
      return data[0].averageNoTenderers;
    } catch (_) {
      return 0;
    }
  }

  getData() {
    const avg = super.getData();
    const { count, t } = this.props;
    if (Number.isNaN(avg) || Number.isNaN(count)) return [];
    return [{
      labels: [
        t('crd:contract:nrBiddersVsAvg:thisLabel'),
        t('crd:contract:nrBiddersVsAvg:avgLabel'),
      ],
      values: [count, avg],
      hoverlabel: {
        bgcolor: '#144361',
      },
      hoverinfo: 'none',
      textinfo: 'none',
      hole: 0.8,
      type: 'pie',
      marker: {
        colors: ['#289df5', '#fac329'],
      },
    }];
  }

  getLayout() {
    return {
      showlegend: false,
      paper_bgcolor: 'rgba(0,0,0,0)',
    };
  }
};

NrOfBidders.Donut.propTypes = {
  t: PropTypes.func.isRequired,
};

NrOfBidders.Donut.endpoint = 'averageNumberOfTenderers';
NrOfBidders.Donut.UPDATABLE_FIELDS = CenterTextDonut.Donut.UPDATABLE_FIELDS.concat('count');

export default NrOfBidders;
