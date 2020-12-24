import React, { useEffect, useState } from 'react';
import FeedbackMessageForm from './feedback';
import { getFeedback, postFeedback } from '../api/Api';
import { getFeedbackUrlPart } from './util';

const FeedbackMessageList = (props) => {
  const [data, setData] = useState(undefined);

  const refreshData = () => {
    getFeedback(getFeedbackUrlPart()).then((result) => {
      setData(result);
    });
  };

  useEffect(() => {
    refreshData();
  }, []);

  const feedbackPoster = (data) => {
    postFeedback({ ...data, department: props.department._id }).then((res) => {
      refreshData();
    }).catch((error) => {
      refreshData();
    });
  };

  return (
    <div className="col-md-offset-1 col-md-10 row">
      <div className="panel panel-default">
        <div className="panel-heading"><h4>Questions or Feedback</h4></div>
        <div className="panel-body">
          <ul>
            {data !== undefined && data.map((d) => (
              <div className="row" key={d.id}>
                <li>
                  <h4>
                    <span>{d.name}</span>
&nbsp;
                    <span
                  className="badge"
                >
                  {d.replies ? d.replies.length : 0}
                </span>
                    <br />
                  </h4>
                  <span>{d.comment}</span>
                  <ul>
                    {d.replies && d.replies.length > 0 && d.replies.map((dd) => (
                  <li key={dd.id}>
                          <h4><span>{dd.name}</span></h4>
                          <span>{dd.comment}</span>
                        </li>
                ))}
                  </ul>
                </li>
                <FeedbackMessageForm replyOpen={false} replyFor={d.id} feedbackPoster={feedbackPoster} />
              </div>
            ))}
          </ul>
          <FeedbackMessageForm replyOpen feedbackPoster={feedbackPoster} />
        </div>
      </div>
    </div>
  );
};

export default FeedbackMessageList;
