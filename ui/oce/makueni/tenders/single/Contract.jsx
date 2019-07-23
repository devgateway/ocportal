class Contract extends React.Component {
  
  render() {
    const { data } = this.props;
  
    if (data === undefined) {
      return null;
    }
  
    const contract = data[0];
    
    return (<div>
      Contract
    </div>);
  }
}

export default Contract;
