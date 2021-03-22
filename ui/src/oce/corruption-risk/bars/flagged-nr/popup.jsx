import React from 'react';
import { withTranslation } from 'react-i18next';

class Popup extends React.PureComponent {
  render() {
    const {
      coordinate, active, viewBox, payload, t,
    } = this.props;
    if (!active || payload == null || !payload[0]) return null;

    const { count, indicatorId } = payload[0].payload;

    const POPUP_HEIGHT = 55;

    const style = {
      left: 0,
      top: coordinate.y - POPUP_HEIGHT - viewBox.top - 4,
      width: 350,
      height: POPUP_HEIGHT,
    };

    const label = count === 1
      ? t('crd:supplier:flaggedNr:popup:sg')
      : t('crd:supplier:flaggedNr:popup:pl');

    const indicatorName = t(`crd:indicators:${indicatorId}:name`);

    return (
      <div>
        <div
          className="crd-popup donut-popup text-center"
          style={style}
        >
          {label.replace('$#$', count)
            .replace('$#$', indicatorName)}
          <div className="arrow" />
        </div>
      </div>
    );
  }
}

export default withTranslation()(Popup);
