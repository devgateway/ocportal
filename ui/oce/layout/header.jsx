import React from 'react';
import translatable from '../translatable';
import cn from 'classnames';
import URI from 'urijs';

import './header.less';
import { API_ROOT, OCE } from '../state/oce-state';

export default class Header extends translatable(React.Component) {
  constructor(props) {
    super(props);

    this.state = {
      exporting: false,
      selected: props.selected || '',
    };

    this.tabs = [
      {
        name: 'tender',
        title: 'Tenders',
        icon: 'assets/icons/efficiency.svg'
      },
      {
        name: 'procurement-plan',
        title: 'Procurement Plan',
        icon: 'assets/icons/compare.svg'
      },
      {
        name: 'm-and-e',
        title: 'Charts',
        icon: 'assets/icons/eprocurement.svg'
      }
    ];

    this.changeOption = this.changeOption.bind(this);
    this.isActive = this.isActive.bind(this);

    this.headerState = OCE.substate({ name: 'headerState' });

    this.statsUrl = this.headerState.input({
      name: 'makueniPPCountEP',
      initial: `${API_ROOT}/makueni/contractStats`,
    });

    this.statsInfo = this.headerState.remote({
      name: 'statsInfo',
      url: this.statsUrl,
    });
  }

  componentDidMount() {
    this.statsInfo.addListener('Header', () => {
      this.statsInfo.getState()
      .then(data => {
        this.setState({ data: data });
      });
    });
  }

  componentWillUnmount() {
    this.statsInfo.removeListener('Header');
  }

  changeOption(option) {
    this.setState({ selected: option });
    this.props.onSwitch(option);
  }

  isActive(option) {
    const { selected } = this.state;
    if (selected === '') {
      return false;
    }
    return selected === option;
  }

  exportBtn() {
    const url = new URI('/api/makueni/excelExport');

    if (this.state.exporting) {
      return (
        <div className="export-progress">
          <span className="export-title">
            Download the Data
          </span>
          <div className="progress">
            <div className="progress-bar progress-bar-danger" role="progressbar"
                 style={{ width: '100%' }}>
              {this.t('export:exporting')}
            </div>
          </div>
        </div>
      );
    }
    return (<div>
        <span className="export-title">
          Download the Data
        </span>
        <div className="export-btn">
        <a href={url} download="export.zip"><button className="xls"></button></a>
          <button className="json"></button>
        </div>
      </div>
    );
  }

  render() {
    const { data } = this.state;
    const currencyFormatter = this.props.styling.tables.currencyFormatter;

    return (<div>
      <header className="branding row">
        <div className="col-sm-8">
          <div className="logo-wrapper">
            <img src="assets/makueni-logo.png" alt="Makueni"/>
          </div>
        </div>

        <div className="col-sm-4">
          <div className="row">
            <div className="navigation">
              {
                this.tabs.map(tab => {
                  return (<a
                      key={tab.name}
                      href="javascript:void(0);"
                      className={cn('', { active: this.isActive(tab.name) })}
                      onClick={() => this.changeOption(tab.name)}
                    >
                      {tab.title}
                    </a>
                  );
                })
              }
            </div>
          </div>
        </div>
      </header>

      <div className="header-tools row">
        {
          data !== undefined
            ? <div>
              <div className="col-md-2 total-item">
                <span className="total-label">Total Contracts</span>
                <span className="total-number">{data.count}</span>
              </div>
              <div className="col-md-4 total-item">
                <span className="total-label">Total Contract Amount</span>
                <span className="total-number">{currencyFormatter(data.value)}</span>
              </div>
            </div>
            : null
        }

        <div className="col-md-3 export">
          {this.exportBtn()}
        </div>
      </div>
    </div>);
  }
}
