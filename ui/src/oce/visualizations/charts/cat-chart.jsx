import { Map } from 'immutable';
import wrap from 'word-wrap';
import Chart from './index';
import backendYearFilterable from '../../backend-year-filterable';
import Comparison from '../../comparison';
import { download } from '../../tools';
import exportBlack from '../../resources/icons/export-black.svg';
import camera from '../../resources/icons/camera.svg';

class CatChart extends backendYearFilterable(Chart) {
  static getCatName(datum) {
    if (this.CAT_WRAP_CHARS !== 0) {
      return wrap(datum.get(this.CAT_NAME_FIELD), { width: this.CAT_WRAP_CHARS, newline: '<br>' });
    }
    return datum.get(this.CAT_NAME_FIELD);
  }

  orientation() {
    return undefined;
  }

  getData() {
    const data = super.getData();
    if (!data) return [];
    const { traceColors, hoverFormatter } = this.props.styling.charts;
    const trace = {
      x: [],
      y: [],
      orientation: this.orientation(),
      type: 'bar',
      marker: {
        color: traceColors[0],
      },
    };

    if (hoverFormatter) {
      trace.text = [];
      trace.hoverinfo = 'text';
    }

    data.forEach((datum) => {
      const catName = this.constructor.getCatName(datum, this.t.bind(this));
      const value = datum.get(this.constructor.CAT_VALUE_FIELD);
      if (this.orientation() === 'h') {
        trace.y.push(catName);
        trace.x.push(value);
      } else {
        trace.x.push(catName);
        trace.y.push(value);
      }
      if (hoverFormatter) trace.text.push(hoverFormatter(value));
    });

    return [trace];
  }
}

class CatChartComparison extends Comparison {
  render() {
    const {
      compareBy, comparisonData, comparisonCriteriaValues, filters, requestNewComparisonData, years, translations,
      styling, width, months, monthly,
    } = this.props;
    if (!comparisonCriteriaValues.length) return null;
    const Component = this.getComponent();
    const decoratedFilters = this.constructor.decorateFilters(filters, compareBy, comparisonCriteriaValues);
    let rangeProp; let
      uniformData;

    if (comparisonData.count() === comparisonCriteriaValues.length + 1) {
      const byCats = comparisonData.map(
        (data) => data.reduce(
          (cats, datum) => cats.set(datum.get(Component.CAT_NAME_FIELD), datum),
          Map(),
        ),
      );

      const cats = comparisonData.reduce(
        (cats, data) => data.reduce(
          (cats, datum) => {
            const cat = datum.get(Component.CAT_NAME_FIELD);
            return cats.set(cat, Map({
              [Component.CAT_NAME_FIELD]: cat,
              [Component.CAT_VALUE_FIELD]: 0,
            }));
          },
          cats,
        ),
        Map(),
      );

      uniformData = byCats.map(
        (data) => cats.merge(data).toList(),
      );

      const maxValue = uniformData.reduce(
        (max, data) => data.reduce(
          (max, datum) => Math.max(max, datum.get(Component.CAT_VALUE_FIELD)),
          max,
        ),
        0,
      );

      rangeProp = {
        yAxisRange: [0, maxValue],
      };
    } else {
      rangeProp = {};
      uniformData = comparisonData;
    }

    return this.wrap(decoratedFilters.map((comparisonFilters, index) => {
      const ref = `visualization${index}`;
      const downloadExcel = () => download({
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
            margin={{ b: 200 }}
            requestNewData={(_, data) => requestNewComparisonData([index], data)}
            data={uniformData.get(index)}
            years={years}
            months={months}
            monthly={monthly}
            title={this.getTitle(index)}
            translations={translations}
            styling={styling}
            width={width / 2}
            {...rangeProp}
          />
          <div className="chart-toolbar">
            <div className="btn btn-default" onClick={downloadExcel}>
              <img src={exportBlack} width="16" height="16" />
            </div>

            <div className="btn btn-default" onClick={() => this.refs[ref].querySelector('.modebar-btn:first-child').click()}>
              <img src={camera} />
            </div>
          </div>
        </div>
      );
    }));
  }
}

CatChart.compareWith = CatChartComparison;

CatChart.UPDATABLE_FIELDS = ['data'];
CatChart.CAT_WRAP_CHARS = 0;

export default CatChart;
