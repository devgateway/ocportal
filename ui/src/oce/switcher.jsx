import PropTypes from 'prop-types';
import React from 'react';
import { getRoute, navigate, onNavigation } from './router';

/**
 * @Deprecated
 */
class OCESwitcher extends React.Component {
  constructor(...args) {
    super(...args);
    this.state = {
      route: getRoute(),
    };

    onNavigation((route) => this.setState({ route }));
  }

  render() {
    const { styling, t, i18n } = this.props;
    const { views } = this.constructor;

    const [optDashboard, ...route] = this.state.route;
    const dashboard = optDashboard || Object.keys(views)[0];
    const CurrentView = views[dashboard];

    return (
      <CurrentView
        onSwitch={navigate}
        styling={styling}
        route={route}
        navigate={navigate.bind(null, dashboard)}
        t={t}
        i18n={i18n}
      />
    );
  }
}

OCESwitcher.propTypes = {
  styling: PropTypes.object.isRequired,
};

OCESwitcher.views = {};

export default OCESwitcher;
