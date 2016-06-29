import React from "react";
import Plot from "../plot";
import {pluck} from "../../tools";

export default class BiddingPeriod extends Plot{
  getTitle() {
    return "Cancelled funding"
  }

  getData(){
    var {data} = this.props;
    return [{
      x: data.map(pluck('year')),
      y: data.map(pluck('count')),
      type: 'scatter',
      fill: 'tonexty'
    }];
  }

  getLayout(){
    return {
      xaxis: {
        title: "Years",
        type: 'category',
        titlefont: {
          color: "#cc3c3b"
        }
      },
      yaxis: {
        title: "Amount",
        titlefont: {
          color: "#cc3c3b"
        },
        range: this.props.yAxisRange
      }
    }
  }

  render(){
    let {pageHeaderTitle, title, actions} = this.props;
    console.log(actions);
    return (
        <section>
          {pageHeaderTitle &&
            <h4 className="page-header">
                {title}
                  &nbsp;
                  <button className="btn btn-default btn-sm" onClick={e => actions.toggleCancelledPercents(true)}>%</button>
            </h4>
          }
          <div ref="chartContainer"></div>
        </section>
    )
  }
}
