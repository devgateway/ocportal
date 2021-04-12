import './makueni.scss';
import React from 'react';
import { Link } from 'react-router-dom';
import { API_ROOT } from '../api/Api';

class ContractsList extends React.PureComponent {
  constructor(props) {
    super(props);

    this.state = {
      data: null,
    };
  }

  componentDidMount() {
    this.fetchData();
  }

  fetchData() {
    const { contractorId, endpointName } = this.props;
    fetch(`${API_ROOT}/${endpointName}?contractorId=${contractorId}`)
      .then((response) => response.json())
      .then((data) => this.setState({ data }));
  }

  row(entry) {
    const id = entry._id;
    return (
      <tr key={id}>
        <td>
          <Link to={`/ui/tender/t/${id}`} className="more-details-link">{entry.title}</Link>
        </td>
      </tr>
    );
  }

  render() {
    const { data } = this.state;
    const { resetContractorID } = this.props;
    return (
      <div>
        <table className="table table-striped table-hover suppliers-table">
          <thead>
            <tr>
              <td>Contract Title</td>
            </tr>
          </thead>
          <tbody>
            {data && data.map((entry) => this.row(entry))}
          </tbody>
        </table>
        <span className="back-text">
          <a onClick={() => resetContractorID()} className="more-details-link">
            Go Back
          </a>
        </span>
      </div>
    );
  }
}

export default ContractsList;
