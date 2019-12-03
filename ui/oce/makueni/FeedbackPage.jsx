import React from 'react';

class FeedbackPage extends React.Component {
  getFeedbackSubject() {
    return escape("Makueni Public Portal Feedback");
  }

  getFeedbackMessage() {
    return (<div className="row feedback-section" >
      <a href={"mailto:someone@example.com?Subject=" + this.getFeedbackSubject()} target="_top">
        <div className="col-md-offset-8 col-md-4" data-intro="Send questions or
        feedback on this project to the government.">
          <div className="pull-right">
            <span>Questions or Feedback</span>
            <img className="feedback-icon" src="assets/icons/feedback.svg" alt="feedback"/>
          </div>
        </div>
      </a>
    </div>);
  }

}

export default FeedbackPage;
