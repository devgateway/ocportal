import React from 'react';
import cn from 'classnames';
import Tab from './tab';
import { cacheFn } from '../../../tools';

export const addTenderDeliveryLocationId = cacheFn(
  (filters, id) => ({
    ...filters,
    locationId: id,
  }),
);

export default class ChartTab extends Tab {
  constructor(props) {
    super(props);
    this.state = {
      chartData: null,
    };
  }

  static getMargins() {
    return {
      t: 0,
      l: 50,
      r: 10,
      b: 50,
    };
  }

  static getChartClass() { return ''; }

  render() {
    const {
      filters, styling, years, t, data, monthly, months,
    } = this.props;
    const decoratedFilters = addTenderDeliveryLocationId(filters, data._id);
    return (
      <div className={cn('map-chart', this.constructor.getChartClass())}>
        <this.constructor.Chart
          filters={decoratedFilters}
          styling={styling}
          years={years}
          monthly={monthly}
          months={months}
          t={t}
          data={this.state.chartData}
          requestNewData={(_, chartData) => this.setState({ chartData })}
          height={250}
          style={{ width: '100%' }}
          layout={{
            autosize: true,
          }}
          margin={this.constructor.getMargins()}
          legend="h"
        />
      </div>
    );
  }
}
