import React from 'react';
import translatable from '../translatable';
import cn from 'classnames';

import './header.less';

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
    if (this.state.exporting) {
      return (
        <div className="export-progress">
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
          <button className="btn btn-default" disabled>
          </button>
        </div>
      </div>
    );
  }

  render() {
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
        <div className="col-md-3 total-item">
          <span className="total-label">Total Tenders</span>
          <span className="total-number">123</span>
        </div>
        <div className="col-md-4 total-item">
          <span className="total-label">Total Tender Amount</span>
          <span className="total-number">100,000</span>
        </div>

        <div className="col-md-2"></div>
        <div className="col-md-3 export">
          {this.exportBtn()}
        </div>
      </div>
    </div>);
  }
}
