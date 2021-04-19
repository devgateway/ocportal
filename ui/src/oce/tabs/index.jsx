import { List, Set } from 'immutable';
import React from 'react';
import PropTypes from 'prop-types';
import Visualization from '../visualization';
import DefaultComparison from '../comparison';
import { download } from '../tools';
import exportBlack from '../resources/icons/export-black.svg';
import camera from '../resources/icons/camera.svg';

class Tab extends Visualization {
  maybeWrap(Component, ref) {
    const { dontWrap, getName } = Component;
    const {
      filters, years, months, t,
    } = this.props;
    const exportable = Component.isChart;
    return dontWrap ? null : (rendered) => (
      <section>
        <h4 className="page-header">
          {getName(t)}
          {exportable && Component.excelEP && false && (
          <img
            src={exportBlack}
            width="16"
            height="16"
            className="chart-export-icon"
            onClick={() => download({
              ep: Component.excelEP,
              filters,
              years,
              months,
              t,
            })}
          />
          )}
          {exportable && (
          <img
            src={camera}
            className="chart-export-icon"
            onClick={() => {
              ref.current.querySelector('.modebar-btn:first-child').click();
            }}
          />
          )}
        </h4>
        {rendered}
      </section>
    );
  }

  compare(Component, index) {
    const {
      compareBy, comparisonData, comparisonCriteriaValues, filters, requestNewComparisonData, years, bidTypes,
      width, t, styling, monthly, months,
    } = this.props;
    const { compareWith: CustomComparison } = Component;
    const Comparison = CustomComparison || DefaultComparison;
    return (
      <Comparison
        key={index}
        compareBy={compareBy}
        comparisonData={comparisonData.get(index, List())}
        comparisonCriteriaValues={comparisonCriteriaValues}
        filters={filters}
        requestNewComparisonData={(path, data) => requestNewComparisonData([index, ...path], data)}
        years={years}
        monthly={monthly}
        months={months}
        Component={Component}
        bidTypes={bidTypes}
        width={width}
        t={t}
        styling={styling}
      />
    );
  }

  render() {
    const {
      filters, compareBy, requestNewData, data, years, months, monthly, width, t, styling,
    } = this.props;
    return (
      <div className="col-sm-12">
        {this.constructor.visualizations.map((Component, index) => {
          const ref = React.createRef();
          return compareBy && Component.comparable ? this.compare(Component, index)
            : (
              <div key={index} ref={ref}>
                <Component
                  filters={filters}
                  requestNewData={(_, data) => requestNewData([index], data)}
                  data={data.get(index)}
                  monthly={monthly}
                  years={years}
                  months={months}
                  width={width}
                  t={t}
                  styling={styling}
                  wrapRendered={this.maybeWrap(Component, ref)}
                  margin={{
                    t: 10, l: 100, b: 80, r: 25, pad: 20,
                  }}
                />
              </div>
            );
        })}
      </div>
    );
  }

  static computeYears(data) {
    if (!data) return Set();
    return this.visualizations.reduce((years, visualization, index) => (visualization.computeYears
      ? years.union(visualization.computeYears(data.get(index)))
      : years),
    Set());
  }

  static computeComparisonYears(data) {
    if (!data) return Set();
    return this.visualizations.reduce((years, visualization, index) => years.union(
      data.get(index, List())
        .reduce((years, data) => (visualization.computeYears
          ? years.union(visualization.computeYears(data))
          : years),
        Set()),
    ),
    Set());
  }

  componentDidMount() {
    window.scrollTo(0, 0);
  }
}

Tab.visualizations = [];

Tab.propTypes = {
  filters: PropTypes.object.isRequired,
  data: PropTypes.object,
  comparisonData: PropTypes.object,
  requestNewData: PropTypes.func.isRequired,
  requestNewComparisonData: PropTypes.func.isRequired,
  compareBy: PropTypes.string.isRequired,
  comparisonCriteriaValues: PropTypes.arrayOf(PropTypes.string).isRequired,
  width: PropTypes.number.isRequired,
  t: PropTypes.func.isRequired,
};

export default Tab;
