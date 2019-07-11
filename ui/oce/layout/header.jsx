import React from 'react';
import translatable from '../translatable';
import cn from 'classnames';

import './Header.less';

export default class Header extends translatable(React.Component) {
  constructor(props) {
    super(props);
  
    this.state = {
      exporting: false,
      selected: props.selected || '',
    };
    
    this.tabs = [
      {
        name: 'makueni',
        title: 'Tenders',
        icon: 'assets/icons/overview.svg'
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
            <i className="glyphicon glyphicon-download-alt"/>
          </button>
        </div>
      </div>
    );
  }
  
  render() {
    console.log(this.state);
    
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
                <span className="circle">
                  <img className="nav-icon" alt="navigation icon" src={tab.icon}/>
                </span>
                      &nbsp;
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
        <div className="col-md-9">
        
        </div>
        <div className="col-md-3 export">
          {this.exportBtn()}
        </div>
      </div>
    </div>);
  }
}
