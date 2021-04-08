import React from 'react';
import cn from 'classnames';
import backendYearFilterable from '../../../../backend-year-filterable';
import Chart from '../../../../visualizations/charts/index';
import './style.scss';

class CenterTextDonut extends React.PureComponent {
  getClassnames() {
    return ['center-text-donut'];
  }

  render() {
    const { Donut } = this.constructor;
    return (
      <div className={cn(this.getClassnames())}>
        <div>
          <Donut
            layout={{
              autosize: true,
            }}
            margin={{
              b: 0, t: 0, r: 0, l: 0, pad: 0,
            }}
            style={{ width: '100%' }}
            layout={{
              autosize: true,
            }}
            height={300}
            {...this.props}
          />
          <div className="center-text">
            {this.getCenterText()}
          </div>
        </div>
        <h4 className="title">
          {this.getTitle()}
        </h4>
      </div>
    );
  }
}

CenterTextDonut.Donut = class extends backendYearFilterable(Chart) {};

export default CenterTextDonut;
