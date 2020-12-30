const React = require('react');

function shallowDiff(a, b) {
  if (a && b && typeof a === 'object' && typeof b === 'object') {
    return Object.keys(a)
      .some((key) => typeof a[key] !== 'function' && a[key] !== b[key]);
  }
  return a !== b;
}

export default class PureRenderComponent extends React.Component {
  shouldComponentUpdate(nextProps, nextState) {
    const shouldUpdate = shallowDiff(this.props, nextProps) || shallowDiff(this.state, nextState);
    if (shouldUpdate) {
      // console.log(Object.keys(this.props).filter(key => this.props[key] != nextProps[key]));
    }
    return shouldUpdate;
  }
}
