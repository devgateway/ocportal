class Award extends React.Component {
  
  render() {
    const { data } = this.props;
  
    if (data === undefined) {
      return null;
    }
  
    const awardAcceptance = data[0];
    
    return (<div>
      Award
    </div>);
  }
}

export default Award;
