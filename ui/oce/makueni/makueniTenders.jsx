import CRDPage from '../corruption-risk/page';
import Header from '../layout/header';

import './makueniTenders.less';

class MakueniTenders extends CRDPage {
  render() {
    return (<div className="container-fluid dashboard-default">
      
      <Header translations={this.props.translations} onSwitch={this.props.onSwitch}
              selected="makueni"/>
              
      <div className="makueni-tenders content row">
        <div className="col-md-12">
          <h1>Makueni Tenders</h1>
        </div>
      </div>
      
    </div>);
  }
}

export default MakueniTenders;
