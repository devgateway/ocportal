import React from "react";
import { cloneChild } from './tools';

class BackendDateFilterable extends React.PureComponent {
  constructor(...args){
    super(...args);
    this.state = this.state || {};
    this.state.decoratedFilters = this.decorateFilters(this.props);
  }

  decorateFilters({ filters, years, months }) {
    const monthly = years.length === 1;
    return {
      ...filters,
      year: years,
      monthly: monthly,
      month: monthly && months.length !== 12 ? months : []
    };
  }

  componentWillUpdate(nextProps) {
    if (['filters', 'years', 'months'].some(prop => this.props[prop] != nextProps[prop])) {
      this.setState({
        decoratedFilters: this.decorateFilters(nextProps),
      })
    }
  }

  render() {
    const decoratedProps = Object.assign({}, this.props, {
      filters: this.state.decoratedFilters,
    });
    delete decoratedProps.years;
    delete decoratedProps.months;
    delete decoratedProps.monthly;
    return cloneChild(this, decoratedProps);
  }
}

export default BackendDateFilterable;