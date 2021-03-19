import React from 'react';
import { pluck } from '../../../../tools';
import Donut from '../../../donut';
import PropTypes from 'prop-types';

class CenterText extends React.Component {
  render() {
    const { data, t } = this.props;
    if (!data) return null;
    const [won, lost] = data.map(pluck('value'));
    const sum = won + lost;
    const percent = (won / sum) * 100;
    return (
      <div className="center-text two-rows">
        <div>
          {won}
          <div className="secondary">
            {t('crd:supplier:nrLostVsWon:center')
              .replace('$#$', sum)
              .replace('$#$', Math.trunc(percent))}
          </div>
        </div>
      </div>
    );
  }
}

CenterText.propTypes = {
  t: PropTypes.func.isRequired,
};

class NrWonVsLost extends React.PureComponent {
  transformNewData(path, data) {
    const won = data.getIn([0, 'won', 'count']);
    const lost = data.getIn([0, 'lostCount']);
    const sum = won + lost;
    const wonPercent = ((won / sum) * 100).toFixed(2);
    const lostPercent = ((lost / sum) * 100).toFixed(2);
    const { t } = this.props;
    this.props.requestNewData(path, [{
      color: '#165781',
      label: t('crd:supplier:nrLostVsWon:won').replace('$#$', won).replace('$#$', wonPercent),
      value: won,
    }, {
      color: '#5fa0c9',
      label: t('crd:supplier:nrLostVsWon:lost').replace('$#$', lost).replace('$#$', lostPercent),
      value: lost,
    }]);
  }

  render() {
    const { t } = this.props;
    return (
      <Donut
        {...this.props}
        requestNewData={this.transformNewData.bind(this)}
        data={this.props.data || []}
        CenterText={CenterText}
        title={t('crd:supplier:nrLostVsWon:title')}
        subtitle={t('crd:supplier:nrLostVsWon:subtitle')}
        endpoint="procurementsWonLost"
      />
    );
  }
}

NrWonVsLost.propTypes = {
  t: PropTypes.func.isRequired,
};

export default NrWonVsLost;
