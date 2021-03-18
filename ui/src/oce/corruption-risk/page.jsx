import React from 'react';

class Page extends React.Component {
  componentDidMount() {
    this.scrollTop();
  }

  scrollTop() {
    window.scrollTo(0, 0);
  }
}

export default Page;
