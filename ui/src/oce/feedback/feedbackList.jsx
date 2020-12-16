import React from 'react';
import { API_ROOT } from '../state/oce-state';
import FeedbackMessageForm from './feedback';
import axios from 'axios';

export const getFeedbackUrlPart  = () =>  {
  return window.location.hash.substr(3);
};

class FeedbackMessageList extends React.PureComponent {

  constructor(props) {
    super(props);

    this.state = {
      data: null
    };
  }

  feedbackPoster(data) {
    let postData = {...data, department: this.props.department._id};
    axios.post(`${API_ROOT}` + '/postFeedback', postData)
      .then(res => {
        this.fetchData();
      }).catch(error => {
      this.fetchData();
    });
  }

  fetchData() {
    fetch( `${API_ROOT}` + '/feedback?page='+ getFeedbackUrlPart())
      .then(response => response.json())
      .then(data => this.setState({ data }));
  }

  componentDidMount() {
    this.fetchData();
  }

  render() {
    const { data } = this.state;
    return(<div className="col-md-offset-1 col-md-10 row">
      <div className="panel panel-default">
        <div className="panel-heading"><h4>Questions or Feedback</h4></div>
        <div className="panel-body">
      <ul>
      {data && data.map(d =>
        <div className="row" key={d.id}>
        <li>
          <h4><span>{d.name}</span>&nbsp;<span className="badge">{d.replies ? d.replies.length:0}</span><br/></h4>
          <span>{d.comment}</span>
          <ul>
          {d.replies && d.replies.length>0 && d.replies.map(dd =>
            <li key={dd.id}>
              <h4><span>{dd.name}</span></h4>
              <span>{dd.comment}</span>
            </li>
          )}
          </ul>
        </li>
        <FeedbackMessageForm replyOpen={false} replyFor={d.id} feedbackPoster=
          {this.feedbackPoster.bind(this)}/>
        </div>
      )}
    </ul>
          <FeedbackMessageForm replyOpen={true} feedbackPoster={this.feedbackPoster.bind(this)}/>
        </div>
      </div>
    </div>);
  }
}
export default FeedbackMessageList;

