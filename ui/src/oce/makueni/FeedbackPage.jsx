import React from 'react';
import translatable from '../translatable';

class FeedbackPage extends translatable(React.Component) {
  getFeedbackSubject() {
    return escape(this.t("feedbackPage:subject"));
  }

  getFeedbackMessage() {
    return (<div className="row feedback-section" >
      <a href={"mailto:opencontracting@makueni.go.ke?Subject=" + this.getFeedbackSubject()} target="_top">
        <div className="col-md-offset-8 col-md-4" data-intro={this.t("feedbackPage:link:dataIntro")}>
          <div className="pull-right">
            <span>{this.t("feedbackPage:link:label")}</span>
            <img className="feedback-icon" src={process.env.PUBLIC_URL + "/icons/feedback.svg"} alt="feedback"/>
          </div>
        </div>
      </a>
    </div>);
  }

}

export default FeedbackPage;
