import React, {useEffect, useState} from "react";
import hoistNonReactStatic from 'hoist-non-react-statics';
import {fmList} from "./state";

/**
 * Render the WrappedComponent only if the corresponding FM entry is visible.
 *
 * You can pass wrapRendered prop to this component to handle the case when you have a wrapper html that should be
 * invisible alongside with the target component.
 *
 * @param WrappedComponent
 * @param fmName
 * @returns {function(*): (*|null)}
 */
const fmConnect = (WrappedComponent, fmName) => {

  const FmComponent = props => {
    const [enabled, setEnabled] = useState(false)

    useEffect(() => {
      fmList.getState('FmComponent')
        .then(entries => {
          setEnabled(entries.indexOf(fmName) >= 0)
        })
    })

    if (enabled) {
      const {wrapRendered, ...nextProps} = props
      return wrapMaybe(wrapRendered, <WrappedComponent {...nextProps} />)
    } else {
      return null
    }
  }

  FmComponent.displayName = `FmComponent(${getDisplayName(WrappedComponent)}, fm=${fmName})`

  hoistNonReactStatic(FmComponent, WrappedComponent)

  return FmComponent
}

function getDisplayName(WrappedComponent) {
  return WrappedComponent.displayName || WrappedComponent.name || 'Component';
}

function wrapMaybe(wrapRendered, rendered) {
  return wrapRendered
    ? wrapRendered(rendered)
    : rendered
}

export default fmConnect
