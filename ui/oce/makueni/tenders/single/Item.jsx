import React from "react";

export const ItemLabel = props => <div className="item-label">{props.children}</div>

export const ItemValue = props => <div className="item-value">{props.children}</div>

export const Item = ({col, className, label, value, labelComponent, valueComponent}) => (
  <div className={"col-md-" + col + " " + className}>
    {labelComponent || <ItemLabel children={label} />}
    {valueComponent || <ItemValue children={value} />}
  </div>
)
