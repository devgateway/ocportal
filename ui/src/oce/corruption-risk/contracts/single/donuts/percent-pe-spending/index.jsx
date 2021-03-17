import CenterTextDonut from '../index';
import PropTypes from 'prop-types';

class PercentPESpending extends CenterTextDonut {
  getCenterText() {
    const { data } = this.props;
    if (!data) return null;
    return (
      <span>
        &nbsp;
        {data.get('percentage').toFixed(2)}
        %
      </span>
    );
  }

  getTitle() {
    const { t } = this.props;
    return t('crd:contract:percentPEspending:name');
  }
}

PercentPESpending.propTypes = {
  t: PropTypes.func.isRequired,
};

PercentPESpending.Donut = class extends CenterTextDonut.Donut {
  getCustomEP() {
    const { procuringEntityId, supplierId } = this.props;
    return `percentageAmountAwarded?procuringEntityId=${procuringEntityId}&supplierId=${supplierId}`;
  }

  transform(data) {
    try {
      const { percentage, totalAwarded, totalAwardedToSuppliers } = data[0];
      return {
        percentage,
        total: totalAwarded.sum,
        toSuppliers: totalAwardedToSuppliers.sum,
      };
    } catch (e) {
      return {
        percentage: 0,
        total: 0,
        toSuppliers: 0,
      };
    }
  }

  componentDidUpdate(prevProps, ...rest) {
    const peChanged = this.props.procuringEntityId !== prevProps.procuringEntityId;
    const supplierChanged = this.props.supplierId !== prevProps.supplierId;
    if (peChanged || supplierChanged) {
      this.fetch();
    } else {
      super.componentDidUpdate(prevProps, ...rest);
    }
  }

  getData() {
    const data = super.getData();
    if (!data || !data.count()) return [];
    const toSuppliers = data.get('toSuppliers');
    const total = data.get('total');
    const { t } = this.props;
    return [{
      labels: [
        t('crd:contract:percentPEspending:this'),
        t('crd:contract:percentPEspending:match'),
      ],
      values: [toSuppliers, total - toSuppliers],
      hoverlabel: {
        bgcolor: '#144361',
      },
      hoverinfo: 'none',
      textinfo: 'none',
      hole: 0.8,
      type: 'pie',
      marker: {
        colors: ['#3371b1', '#289df5'],
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

PercentPESpending.Donut.propTypes = {
  t: PropTypes.func.isRequired,
};

export default PercentPESpending;
