import './makueni.less';
import React from 'react';
import {API_ROOT} from '../state/oce-state';

class ContractsList extends React.PureComponent {

  constructor(props) {
    super(props);

    this.state = {
      data: null
    };
  }

  fetchData() {
    const { contractorId, endpointName } = this.props;
    fetch(`${API_ROOT}` + '/' + endpointName + '?' + 'contractorId=' + contractorId)
      .then(response => response.json())
      .then(data => this.setState({ data }));
  }

  componentDidMount() {
    this.fetchData();
  }

  row(entry) {
    const id = entry._id;
    return (<tr key={id}>
      <td>
        <a href={`#!/tender/t/${id}`} className="more-details-link">{entry.title}</a>
        </td>
    </tr>);
  }

  render() {
    const { data } = this.state;
    const { resetContractorID } = this.props;
    return (<div>
      <table className="table table-striped table-hover suppliers-table">
        <thead>
        <tr>
          <td>Contract Title</td>
        </tr>
        </thead>
        <tbody>
        {data && data.map(entry => this.row(entry))}
        </tbody>
      </table>
      <span className="back-text">
        <a onClick={e => resetContractorID()} className="more-details-link">
        Go Back
        </a>
        </span>
    </div>);
  }
}

export default ContractsList;
