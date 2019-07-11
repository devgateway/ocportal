import React from 'react';
import translatable from '../translatable';

import './Header.less';

export default class Header extends translatable(React.Component) {
  constructor(props) {
    super(props);
    
    this.state = {
      exporting: false,
      selected: props.section || '',
    };
    
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
        <div className="col-sm-6">
          <div className="logo-wrapper">
            <img src="assets/makueni-logo.png" alt="Makueni"/>
          </div>
        </div>
        
        <div className="col-sm-6">
          <div className="row">
            <div className="navigation">
              <div onClick={() => this.changeOption('makueni')}>
                Tenders
              </div>
              
              <div onClick={() => this.changeOption('m-and-e')}>
                Charts
              </div>
            
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
