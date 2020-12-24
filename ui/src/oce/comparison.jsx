import { List, Set, Map } from 'immutable';
import PureRenderCompoent from './pure-render-component';
import translatable from './translatable';
import { max, cacheFn, download } from './tools';
import orgNamesFetching from './orgnames-fetching';
import exportBlack from './resources/icons/export-black.svg';
import camera from './resources/icons/camera.svg';

class Comparison extends orgNamesFetching(translatable(PureRenderCompoent)) {
  getComponent() {
    return this.props.Component;
  }

  wrap(children) {
    return (
      <div>
        <h3 className="page-header">{this.getComponent().getName(this.t.bind(this))}</h3>
        <div className="row">
          {children}
        </div>
      </div>
    );
  }

  getOrgsWithoutNamesIds() {
    const { comparisonCriteriaValues, compareBy } = this.props;
    return compareBy == 'procuringEntityId'
      ? comparisonCriteriaValues.filter((id) => !this.state.orgNames[id])
      : [];
  }

  getTitle(index) {
    const { compareBy, bidTypes, comparisonCriteriaValues } = this.props;
    if (compareBy == 'bidTypeId') {
      return bidTypes.get(comparisonCriteriaValues[index], this.t('general:comparison:other'));
    } if (compareBy == 'procuringEntityId') {
      const orgId = comparisonCriteriaValues[index];
      return this.state.orgNames[orgId] || orgId || this.t('general:comparison:other');
    }
    return comparisonCriteriaValues[index] || this.t('general:comparison:other');
  }

  render() {
    const {
      compareBy, comparisonData, comparisonCriteriaValues, filters, requestNewComparisonData, years, width,
      translations, styling, monthly, months,
    } = this.props;
    if (!comparisonCriteriaValues.length) return null;
    const Component = this.getComponent();
    const decoratedFilters = this.constructor.decorateFilters(filters, compareBy, comparisonCriteriaValues);
    let rangeProp; let
      uniformData;

    if (comparisonData.count() == comparisonCriteriaValues.length + 1) {
      uniformData = monthly
        ? this.constructor.mkUniformData(Component, 'month', comparisonData, months)
        : this.constructor.mkUniformData(Component, 'year', comparisonData, years);

      const maxValue = uniformData.map((datum) => datum.map(Component.getMaxField).reduce(max, 0)).reduce(max, 0);

      rangeProp = {
        [Component.horizontal ? 'xAxisRange' : 'yAxisRange']: [0, maxValue],
      };
    } else {
      uniformData = Map();
      rangeProp = {};
    }

    return this.wrap(decoratedFilters.map((comparisonFilters, index) => {
      const ref = `visualization${index}`;
      const downloadExcel = (e) => download({
        ep: Component.excelEP,
        filters: comparisonFilters,
        years,
        months,
        t: this.t.bind(this),
      });
      return (
        <div className="col-md-6 comparison" key={index} ref={ref}>
          <Component
            filters={comparisonFilters}
            requestNewData={(_, data) => requestNewComparisonData([index], data)}
            data={uniformData.get(index)}
            years={years}
            monthly={monthly}
            months={months}
            title={this.getTitle(index)}
            width={width / 2}
            translations={translations}
            styling={styling}
            legend="h"
            {...rangeProp}
          />
          <div className="chart-toolbar">
            {Component.excelEP && (
            <div className="btn btn-default" onClick={downloadExcel}>
              <img src={exportBlack} width="16" height="16" />
            </div>
            )}

            <div className="btn btn-default" onClick={(e) => this.refs[ref].querySelector('.modebar-btn:first-child').click()}>
              <img src={camera} />
            </div>
          </div>
        </div>
      );
    }));
  }
}

function getInverseFilter(filter) {
  switch (filter) {
    case 'bidTypeId': return 'notBidTypeId';
    case 'bidSelectionMethod': return 'notBidSelectionMethod';
    case 'procuringEntityId': return 'notProcuringEntityId';
  }
}

/**
 *
 * @param Component A visualization or any object that implement `getFillerDatum` method
 * @param dateName A string associated with the date type, i.e. "month" or "year"
 * @param comparisonData An array of chart data
 * @param dates A set of selected dates
 */
Comparison.mkUniformData = (Component, dateName, comparisonData, dates) => comparisonData.map((comparisonDatum) => // map each chart data array ...
  dates.map((date) => // ...to a mapping of dates, where each element is either...
    comparisonDatum.find((datum) => datum.get(dateName) == date) // the chart datum for the said date
                || Component.getFillerDatum({ [dateName]: +date })) // or a dummy, if no datum for that date can be found
    .toList());

Comparison.decorateFilters = cacheFn((filters, compareBy, comparisonCriteriaValues) => List(comparisonCriteriaValues)
  .map((criteriaValue) => filters.set(compareBy, criteriaValue))
  .push(filters.set(getInverseFilter(compareBy), comparisonCriteriaValues)));

export default Comparison;
