import React from 'react';
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  LabelList,
  ResponsiveContainer,
  Legend,
  Tooltip,
} from 'recharts';
import { tCreator } from '../../../translatable';
import Popup from './popup';
import { renderTopLeftLabel } from '../../archive/tools';

const WinsAndFlags = ({
  translations, zoomed, data, length,
}) => {
  let height = 350;
  let slicedData = data;
  if (zoomed) {
    height = Math.max(height, data.length * 50);
  } else {
    slicedData = data.slice(0, length);
    if (slicedData.length < length) {
      for (let counter = slicedData.length; counter < length; counter += 1) {
        slicedData.unshift({});
      }
    }

    height = Math.max(length * 70, 200);
  }

  const t = tCreator(translations);

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
              barSize={zoomed ? 5 : 10}
              barGap={0}
              barCategoryGap={15}
            >
              <XAxis type="number" />
              <YAxis type="category" hide dataKey="name" />
              <Tooltip content={<Popup />} translations={translations} cursor={false} />
              <Legend
                align="left"
                verticalAlign="top"
                height={30}
                iconType="square"
              />
              <Bar
                name={t('crd:suppliers:wins')}
                dataKey="wins"
                fill="#289df4"
                minPointSize={3}
                isAnimationActive={false}
              >
                <LabelList
                  dataKey="name"
                  position="insideTopLeft"
                  content={renderTopLeftLabel}
                />
              </Bar>
              <Bar
                name={t('crd:contracts:baseInfo:flag:pl')}
                dataKey="flags"
                fill="#ce4747"
                minPointSize={3}
              />
            </BarChart>
          </ResponsiveContainer>
        )}
    </div>
  );
};

export default WinsAndFlags;
