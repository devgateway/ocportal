class PurchaseReq extends React.Component {
  
  render() {
    const { data } = this.props;
  
    if (data === undefined) {
      return null;
    }
  
    console.log(data);
    
    return (<div>
      <div className="row padding-top-10">
        <div className="col-md-6">
          <div className="item-label">Purchase Requisition Title</div>
          <div className="item-value">{data.title}</div>
        </div>
        <div className="col-md-6">
          <div className="item-label">Purchase Request Number</div>
          <div className="item-value">{data.purchaseRequestNumber}</div>
        </div>
      </div>
    </div>);
  }
}

export default PurchaseReq;
