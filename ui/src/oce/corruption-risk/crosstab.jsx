import cn from 'classnames';
import PropTypes from 'prop-types';
import Table from '../visualizations/tables/index';

class Crosstab extends Table {
  buildUrl(ep) {
    const url = super.buildUrl(ep);
    const { indicators } = this.props;
    url.addSearch('flags', indicators);
    return url;
  }

  componentDidUpdate(prevProps, ...args) {
    if (this.props.indicators !== prevProps.indicators) {
      this.fetch();
    }
    super.componentDidUpdate(prevProps, ...args);
  }

  transform(data) {
    const { indicators } = this.props;
    const matrix = {};
    let y = 0;
    for (let x = 0; x < indicators.length; x += 1) {
      const xIndicatorID = indicators[x];
      matrix[xIndicatorID] = {};
      const xArray = data[xIndicatorID];
      for (y = 0; y < indicators.length; y += 1) {
        const yIndicatorID = indicators[y];
        if (xArray && xArray.length > 0) {
          const datum = xArray[0];
          matrix[xIndicatorID][yIndicatorID] = {
            count: datum[yIndicatorID],
            percent: datum.percent[yIndicatorID],
          };
        } else {
          matrix[xIndicatorID][yIndicatorID] = {
            count: 0,
            percent: 0,
          };
        }
      }
    }
    return matrix;
  }

  row(rowData, rowIndicatorID) {
    const { t } = this.props;
    const rowIndicatorName = t(`crd:indicators:${rowIndicatorID}:name`);
    const rowIndicatorDescription = t(`crd:indicators:${rowIndicatorID}:indicator`);
    const lastKey = rowData.lastKeyOf(rowData.last());
    return (
      <tr key={rowIndicatorID}>
        <td>{rowIndicatorName}</td>
        <td className="nr-flags">{rowData.getIn([rowIndicatorID, 'count'])}</td>
        {rowData.map((datum, indicatorID) => {
          const indicatorName = t(`crd:indicators:${indicatorID}:name`);
          const indicatorDescription = t(`crd:indicators:${indicatorID}:indicator`);
          if (indicatorID === rowIndicatorID) {
            return <td className="not-applicable" key={indicatorID}>&mdash;</td>;
          }
          const percent = datum.get('percent');
          const count = datum.get('count');
          const color = Math.round(255 - 255 * (percent / 100));
          const style = { backgroundColor: `rgb(${color}, 255, ${color})` };
          const isLast = indicatorID === lastKey;
          return (
            <td key={indicatorID} className={cn('hoverable', { 'popup-left': isLast })} style={style}>
              {percent && percent.toFixed(2)}
              {' '}
              %
              <div className="crd-popup text-left">
                <div className="row">
                  <div className="col-sm-12 info">
                    {t('crd:corruptionType:crosstab:popup:percents')
                      .replace('$#$', percent.toFixed(2))
                      .replace('$#$', rowIndicatorName)
                      .replace('$#$', indicatorName)}
                  </div>
                  <div className="col-sm-12">
                    <hr />
                  </div>
                  <div className="col-sm-12 info">
                    <h4>{t('crd:corruptionType:crosstab:popup:count').replace('$#$', count)}</h4>
                    <p>
                      <strong>{rowIndicatorName}</strong>
                      :
                      {' '}
                      {rowIndicatorDescription}
                    </p>
                    <p className="and">{t('crd:corruptionType:crosstab:popup:and')}</p>
                    <p>
                      <strong>{indicatorName}</strong>
                      :
                      {' '}
                      {indicatorDescription}
                    </p>
                  </div>
                </div>
                <div className="arrow" />
              </div>
            </td>
          );
        }).toArray()}
      </tr>
    );
  }

  render() {
    const { data, t } = this.props;
    if (!data) return null;
    if (!data.count()) return null;
    return (
      <div className="crosstab-container">
        <table className="table table-striped table-hover table-bordered table-crosstab">
          <thead>
            <tr>
              <th className="name-column" />
              <th className="flags-column"># Flags</th>
              {data.map((_, indicatorID) => (
                <th className="percent-column" key={indicatorID}>{t(`crd:indicators:${indicatorID}:name`)}</th>
              )).toArray()}
            </tr>
          </thead>
          <tbody>
            {data.map((rowData, indicatorID) => this.row(rowData, indicatorID)).toArray()}
          </tbody>
        </table>
      </div>
    );
  }
}

Crosstab.endpoint = 'flags/crosstab';

Crosstab.propTypes = {
  t: PropTypes.func.isRequired,
};

export default Crosstab;
