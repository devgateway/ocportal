import React from 'react';
import { List } from 'immutable';
import Donut from '../../../donut';
import { pluck } from '../../../../tools';
import PropTypes from 'prop-types';

class CenterText extends React.PureComponent {
  render() {
    const { data } = this.props;
    let style = {};
    const label = data.map(pluck('value')).join('/');
    if (label.length > 9) {
      style = { fontSize: '2vw' };
    }

    return (
      <div className="center-text two-rows total-flags-center-text" style={style}>
        {data.map(({ color, value }) => <span key={color} style={{ color }}>{value}</span>)}
      </div>
    );
  }
}

const COLORS = ['#fbc42c', '#3372b2', '#30a0f5'];

class TotalFlags extends React.PureComponent {
  render() {
    const { t } = this.props;
    const data = (this.props.data || List()).map((datum, index) => {
      const value = datum.get('indicatorCount');
      const indicatorName = t(`crd:corruptionType:${datum.get('type')}:name`);
      const label = value === 1
        ? t('crd:supplier:nrFlags:label:sg')
        : t('crd:supplier:nrFlags:label:pl');

      return {
        color: COLORS[index],
        label: label.replace('$#$', value).replace('$#$', indicatorName),
        value: datum.get('indicatorCount'),
      };
    }).toJS();
    return (
      <Donut
        {...this.props}
        data={data}
        CenterText={CenterText}
        endpoint="totalFlaggedIndicatorsByIndicatorType"
        title={t('crd:supplier:nrFlags:title')}
        subtitle={t('crd:supplier:nrFlags:subtitle')}
      />
    );
  }
}

TotalFlags.propTypes = {
  t: PropTypes.func.isRequired,
};

export default TotalFlags;
