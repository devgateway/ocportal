import CRDPage from '../corruption-risk/page';
import Header from '../layout/header';

import './alerts.less';

class Alerts extends CRDPage {
  
  constructor(props) {
    super(props);
    
    this.state = {
      data: []
    };
  }
  
  
  render() {
    
    return (<div className="container-fluid dashboard-default">
        
        <Header translations={this.props.translations} onSwitch={this.props.onSwitch}
                styling={this.props.styling} selected=""/>
        
        
        <div className="alerts content row">
          <div className="col-md-12">
            <h1>Alerts</h1>
          </div>
        </div>
      </div>
    );
  }
}

export default Alerts;
