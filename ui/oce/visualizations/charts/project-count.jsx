import FrontendDateFilterableChart from "./frontend-date-filterable";
import {pluckImm} from "../../tools";

class ProjectCount extends FrontendDateFilterableChart{
  getData(){
    let data = super.getData();
    if(!data) return [];

    const monthly = data.hasIn([0, 'month']);
    const dates = monthly ?
        data.map(pluckImm('month')).map(month => this.t(`general:months:${month}`)).toArray() :
        data.map(pluckImm('year')).toArray();

    return [{
      x: dates,
      y: data.map(pluckImm('count')).toArray(),
      type: 'bar',
      fill: 'tonexty',
      marker: {
        color: this.props.styling.charts.traceColors[0]
      }
    }];
  }

  getLayout(){
    const {hoverFormat} = this.props.styling.charts;
    return {
      xaxis: {
        title: this.props.monthly ? this.t('general:month') : this.t('general:year'),
        type: 'category'
      },
      yaxis: {
        title: this.t('charts:projectCount:yAxisTitle'),
        hoverformat: hoverFormat,
        tickprefix: "   "
      }
    }
  }
}

ProjectCount.endpoint = 'numberOfProjectsByYear';
ProjectCount.excelEP = 'numberOfProjectsByYearExcelChart';
ProjectCount.getName = t => t('charts:projectCount:title');
//ProjectCount.getMaxField = pluckImm('totalTendersUsingEbid');

export default ProjectCount;
