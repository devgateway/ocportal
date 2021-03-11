import React from 'react';
import {
  BarChart, Bar, XAxis, YAxis, LabelList, ResponsiveContainer, Tooltip,
} from 'recharts';
import { tCreator } from '../../../translatable';
import Popup from './popup';
import { renderTopLeftLabel } from '../../archive/tools';

const ProcurementsByStatus = ({
  data, length, translations, zoomed,
}) => {
  const t = tCreator(translations);

  let height = 350;
  let slicedData;
  if (zoomed) {
    slicedData = data;
    height = Math.max(height, slicedData.length * 50);
  } else {
    slicedData = data.slice(0, 5);
    if (slicedData.length < length) {
      for (let counter = slicedData.length; counter < length; counter += 1) {
        slicedData.unshift({});
      }
    }
    height = length * 80;
  }

  return (
    <div className="oce-chart">
      {(data === undefined || data.length === 0)
        ? (
          <div className="row">
            <br />
            <div className="col-md-12">
              <div className="message">{t('charts:general:noData')}</div>
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
              <YAxis type="category" hide dataKey="status" />
              <Tooltip content={<Popup />} translations={translations} cursor={false} />
              <Bar
                name={t('crd:procuringEntities:byStatus:title')}
                dataKey="count"
                fill="#289df4"
                minPointSize={3}
                isAnimationActive={false}
              >
                <LabelList
                  dataKey="status"
                  position="insideTopLeft"
                  content={renderTopLeftLabel}
                />
              </Bar>
            </BarChart>
          </ResponsiveContainer>
        )}
    </div>
  );
};

export default ProcurementsByStatus;
