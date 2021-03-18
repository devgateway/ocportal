import CenterTextDonut from './index';
import PropTypes from 'prop-types';

class NrOfContractsWithPE extends CenterTextDonut {
  getClassnames() {
    return super.getClassnames().concat('nr-contracts');
  }

  getCenterText() {
    const { data, t } = this.props;
    if (!data) return null;
    return (
      <div>
        {data.get('thisPE')}
        <div className="secondary">
          {t('crd:contract:nrContractWithPE:of')}
          &nbsp;
          {data.get('total')}
        </div>
      </div>
    );
  }

  getTitle() {
    const { t } = this.props;
    return t('crd:contract:nrContractWithPE:name');
  }
}

NrOfContractsWithPE.propTypes = {
  t: PropTypes.func.isRequired,
};

NrOfContractsWithPE.Donut = class extends CenterTextDonut.Donut {
  getCustomEP() {
    const { procuringEntityId, supplierId } = this.props;
    if (!procuringEntityId || !supplierId) return [];
    return [
      `ocds/release/count/?procuringEntityId=${procuringEntityId}`,
      `ocds/release/count/?procuringEntityId=${procuringEntityId}`
        + `&supplierId=${supplierId}&awardStatus=active`,
    ];
  }

  transform([total, thisPE]) {
    return {
      thisPE,
      total,
    };
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
    if (!data) return [];
    const { t } = this.props;
    return [{
      labels: [
        t('crd:contract:nrContractWithPE:match'),
        t('crd:contract:nrContractWithPE:total'),
      ],
      values: [data.get('thisPE'), data.get('total')],
      hoverlabel: {
        bgcolor: '#144361',
      },
      hoverinfo: 'none',
      textinfo: 'none',
      hole: 0.8,
      type: 'pie',
      marker: {
        colors: ['#72c47e', '#2e833a'],
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

NrOfContractsWithPE.Donut.propTypes = {
  t: PropTypes.func.isRequired,
};

export default NrOfContractsWithPE;
