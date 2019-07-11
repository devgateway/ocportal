import CRDPage from '../corruption-risk/page';
import Header from '../layout/header';

class MakueniTenders extends CRDPage {
  render() {
    console.log(this.props);
    
    return (<div className="container-fluid dashboard-default">
      <Header translations={this.props.translations} onSwitch={this.props.onSwitch}/>
      <div className="makueni-tenders row">
        <div className="col-md-12">
          <h1>Makueni Tenders</h1>
        </div>
      </div>
    </div>);
  }
}

export default MakueniTenders;
