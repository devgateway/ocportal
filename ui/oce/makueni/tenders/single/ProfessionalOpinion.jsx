class ProfessionalOpinion extends React.Component {
  
  render() {
    const { data } = this.props;
  
    if (data === undefined) {
      return null;
    }
  
    const professionalOpinion = data[0];
    
    return (<div>
      Professional Opinion
    </div>);
  }
}

export default ProfessionalOpinion;
