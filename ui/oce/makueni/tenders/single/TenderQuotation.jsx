class TenderQuotation extends React.Component {
  
  render() {
    const { data } = this.props;
    
    if (data === undefined) {
      return null;
    }
    
    const tenderQuotationEvaluation = data[0];
    
    console.log(tenderQuotationEvaluation);
    
    return (<div>
      <div className="row padding-top-10">
        <div className="col-md-6">
          <div className="item-label">Closing Date</div>
          <div
            className="item-value">{new Date(tenderQuotationEvaluation.closingDate).toLocaleDateString()}</div>
        </div>
      </div>
    </div>);
  }
}

export default TenderQuotation;
