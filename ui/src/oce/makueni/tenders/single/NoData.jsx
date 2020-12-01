import React from 'react';
import translatable from "../../../translatable";

class NoDataMessage extends translatable(React.Component) {
render() {
    return (
      <div className="no-data-wrapper">
        <h2>{this.t("general:noData")}</h2>
      </div>
    )
  }
}


export default NoDataMessage;
