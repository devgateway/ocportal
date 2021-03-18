import React from 'react';
import { Set } from 'immutable';
import ReactDOM from 'react-dom';
import PropTypes from 'prop-types';
import Amounts from './amounts';
import Percents from './percents';
import Comparison from '../../../comparison';
import camera from '../../../resources/icons/camera.svg';

class Cancelled extends React.Component {
  static computeYears(data) {
    if (!data) return Set();
    return Amounts.computeYears(data).union(Percents.computeYears(data));
  }

  constructor(props) {
    super(props);
    this.state = {
      percents: false,
    };
  }

  render() {
    const { percents } = this.state;
    const { t } = this.props;
    const Chart = percents ? Percents : Amounts;
    return (
      <section>
        <h4 className="page-header">
          {percents ? t('charts:cancelledPercents:title') : t('charts:cancelledAmounts:title')}
        &nbsp;
          <button
            className="btn btn-default btn-sm"
            onClick={() => this.setState({ percents: !percents })}
            dangerouslySetInnerHTML={{ __html: percents ? '&#8363;' : '%' }}
          />
          {/* <img */}
          {/*    src="assets/icons/export-black.svg" */}
          {/*    width="16" */}
          {/*    height="16" */}
          {/*    className="chart-export-icon" */}
          {/*    onClick={e => download({ */}
          {/*      ep: Chart.excelEP, */}
          {/*      filters, */}
          {/*      years, */}
          {/*      months, */}
          {/*      t: t */}
          {/*    })} */}
          {/* /> */}

          <img
            src={camera}
            className="chart-export-icon"
            onClick={() => ReactDOM.findDOMNode(this).querySelector('.modebar-btn:first-child').click()}
          />
        </h4>
        <Chart {...this.props} />
      </section>
    );
  }
}

Cancelled.dontWrap = true;
Cancelled.comparable = true;
Cancelled.compareWith = class CancelledComparison extends Comparison {
  constructor(props) {
    super(props);
    this.state.percents = false;
  }

  getComponent() {
    return this.state.percents ? Percents : Amounts;
  }

  wrap(children) {
    const { percents } = this.state;
    const { t } = this.props;
    return (
      <div>
        <h3 className="page-header">
          {percents ? t('charts:cancelledPercents:title') : t('charts:cancelledAmounts:title')}
          {percents ? t('charts:cancelledPercents:title') : t('charts:cancelledAmounts:title')}
        &nbsp;
          <button
            className="btn btn-default btn-sm"
            onClick={() => this.setState({ percents: !percents })}
            dangerouslySetInnerHTML={{ __html: percents ? '&#8363;' : '%' }}
          />
        </h3>
        <div className="row">
          {children}
        </div>
      </div>
    );
  }
};

Cancelled.propTypes = {
  t: PropTypes.func.isRequired,
};

export default Cancelled;
