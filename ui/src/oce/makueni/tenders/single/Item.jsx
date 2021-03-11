import React from 'react';

export const ItemLabel = (props) => <div className="item-label">{props.children}</div>;

export const ItemValue = (props) => <div className="item-value">{props.children}</div>;

export const Item = ({
  col, label, labelComponent, value, children,
}) => (
  <div className={`col-md-${col}`}>
    {labelComponent || <ItemLabel children={label} />}
    <ItemValue>
      {children || value || '-'}
    </ItemValue>
  </div>
);
