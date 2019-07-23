class Notification extends React.Component {
  
  render() {
    const { data } = this.props;
  
    if (data === undefined) {
      return null;
    }
  
    const awardNotification = data[0];
    
    return (<div>
      Notification
    </div>);
  }
}

export default Notification;
