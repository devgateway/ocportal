import React from 'react';
import {tCreator} from "../../../translatable";

const NoDataMessage = (props) => {
    const t = tCreator(props.translations);
    return (
        <div className="no-data-wrapper">
            <h2>{t("general:noData")}</h2>
        </div>
    );
}

export default NoDataMessage;