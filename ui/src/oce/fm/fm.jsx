import React, { useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import hoistNonReactStatic from 'hoist-non-react-statics';
import { LOADED, loadFM, selectFM } from './fmSlice';

function wrapMaybe(wrapRendered, rendered) {
  return wrapRendered
    ? wrapRendered(rendered)
    : rendered;
}

function getDisplayName(WrappedComponent) {
  return WrappedComponent.displayName || WrappedComponent.name || 'Component';
}

/**
 * Render the WrappedComponent after FM entries were loaded.
 *
 * WrappedComponent will receive isFeatureVisible prop which is a function that accepts one argument, the FM name.
 *
 * If fmName parameter is specified, then WrappedComponent will be rendered only when this feature is visible.
 *
 * You can pass wrapRendered prop to this component to handle the case when you have a wrapper html that should be
 * invisible alongside with the target component.
 *
 * @param WrappedComponent
 * @param fmName
 * @returns {function(*): (*|null)}
 */
const fmConnect = (WrappedComponent, fmName) => {
  const FmComponent = (props) => {
    const dispatch = useDispatch();
    const fm = useSelector(selectFM);

    useEffect(() => {
      dispatch(loadFM());
    }, [dispatch]);

    const isFeatureVisible = (name) => fm.list.indexOf(name) >= 0;

    if (fm.status === LOADED && (fmName === undefined || isFeatureVisible(fmName))) {
      const { wrapRendered, ...nextProps } = props;
      return wrapMaybe(wrapRendered,
        <WrappedComponent
          {...nextProps}
          isFeatureVisible={isFeatureVisible}
        />);
    }
    return null;
  };

  FmComponent.displayName = `FmComponent(${getDisplayName(WrappedComponent)}, fm=${fmName})`;

  hoistNonReactStatic(FmComponent, WrappedComponent);

  return FmComponent;
};

export default fmConnect;
