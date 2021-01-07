import React from 'react';
import {
  ResponsiveContainer,
  BarChart,
  XAxis,
  YAxis,
  Legend,
  Bar,
  LabelList,
  Tooltip,
} from 'recharts';
import translatable, { tCreator } from '../../../translatable';
import { renderTopLeftLabel } from '../../archive/tools';
import Popup from './popup';

const corruptionTypeColors = {
  FRAUD: '#299df4',
  RIGGING: '#3372b2',
  COLLUSION: '#fbc42c',
};

function mkGradient(id, colors) {
  const stops = [];
  const step = 100 / colors.length;
  colors.forEach((color, index) => {
    stops.push({
      color,
      offset: step * index + 1,
    });
    stops.push({
      color,
      offset: step * (index + 1) - 1,
    });
  });

  return (
    <linearGradient id={id} x1="0%" y1="0%" x2="100%" y2="100%">
      {stops.map(({ color, offset }, index) => (
        <stop
          key={index}
          offset={`${offset}%`}
          style={{
            stopColor: color,
            stopOpacity: 1,
          }}
        />
      ))}
    </linearGradient>
  );
}

class TaggedBar extends translatable(Bar) {
  maybeGetGradients(types) {
    return mkGradient(
      this.getGradientId(types),
      types.map((type) => corruptionTypeColors[type]),
    );
  }

  getGradientId(types) {
    return `gradient_${types.join('_')}`;
  }

  getFill(types) {
    if (types.length === 1) {
      const [type] = types;
      return corruptionTypeColors[type];
    }
    return `url(#${this.getGradientId(types)})`;
  }

  renderRectangle(option, props) {
    const { types } = props;
    return (
      <g>
        {this.maybeGetGradients(types)}
        {super.renderRectangle(option, {
          ...props,
          fill: this.getFill(types),
        })}
      </g>
    );
  }
}

const FlaggedNr = ({
  data, length, zoomed, translations,
}) => {
  let height = 350;
  let slicedData;
  if (zoomed) {
    slicedData = data;
    height = Math.max(height, data.length * 50);
  } else {
    slicedData = data.slice(0, length);
    if (slicedData.length < length) {
      for (let counter = slicedData.length; counter < length; counter += 1) {
        slicedData.unshift({ types: [] });
      }
    }

    height = Math.max(length * 70, 200);
  }

  const corruptionTypes = new Set();
  slicedData.forEach(
    (datum) => datum.types.forEach(
      (type) => corruptionTypes.add(type),
    ),
  );

  const t = tCreator(translations);

  const legendPayload = [...corruptionTypes].map(
    (corruptionType) => ({
      value: t(`crd:corruptionType:${corruptionType}:name`),
      type: 'square',
      color: corruptionTypeColors[corruptionType],
    }),
  );

  return (
    <div className="oce-chart">
      {(data === undefined || data.length === 0)
        ? (
          <div className="row">
            <br />
            <div className="col-md-12">
              <div className="message">No data</div>
            </div>
            <br />
          </div>
        )
        : (
          <ResponsiveContainer width="100%" height={height}>
            <BarChart
              layout="vertical"
              data={slicedData}
              barSize={zoomed ? 10 : 20}
              barGap={0}
              barCategoryGap={15}
            >
              <XAxis type="number" />
              <YAxis type="category" dataKey="indicatorId" hide />
              <Tooltip content={<Popup />} translations={translations} cursor={false} />
              <Legend
                align="left"
                verticalAlign="top"
                payload={legendPayload}
                height={30}
              />
              <TaggedBar
                dataKey="count"
                minPointSize={3}
                isAnimationActive={false}
              >
                <LabelList
                  formatter={(indicatorId) => t(`crd:indicators:${indicatorId}:name`)}
                  dataKey="indicatorId"
                  position="insideTopLeft"
                  content={renderTopLeftLabel}
                />
              </TaggedBar>
            </BarChart>
          </ResponsiveContainer>
        )}
    </div>
  );
};

export default FlaggedNr;
