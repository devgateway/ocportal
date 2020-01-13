import React from 'react';
import { API_ROOT } from '../state/oce-state';
import FeedbackMessageForm from './feedback';

class FeedbackMessageList extends React.PureComponent {

  constructor(props) {
    super(props);

    this.state = {
      data: null,
    };

  }

  componentDidMount() {
    fetch( `${API_ROOT}` + '/feedback?page=tender/t/33921',)
      .then(response => response.json())
      .then(data => this.setState({ data }));
  }

  render() {
    const { data } = this.state;
    return(<div className="col-md-offset-1 col-md-10 row">
      <div className="panel panel-default">
        <div className="panel-heading"><h4>Feedback</h4></div>
        <div className="panel-body">
      <ul>
      {data && data.map(d =>
        <div className="row" key={d.id}>
        <li>
          <h4><span>{d.name}</span>&nbsp;<span className="badge">{d.replies.length}</span><br/></h4>
          <span>{d.comment}</span>
          <ul>
          {d.replies.length>0 && d.replies.map(dd =>
            <li key={dd.id}>
              <h4><span>{dd.name}</span></h4>
              <span>{dd.comment}</span>
            </li>
          )}
          </ul>
        </li>
        <FeedbackMessageForm replyOpen={false} replyFor={d.id}/>
        </div>
      )}
    </ul>
          <FeedbackMessageForm replyOpen={true}/>
        </div>
      </div>
    </div>);
  }
}
export default FeedbackMessageList;

