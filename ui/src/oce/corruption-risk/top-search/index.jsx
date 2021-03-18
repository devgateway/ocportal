import React from 'react';
import PropTypes from 'prop-types';
import './style.scss';
import fmConnect from '../../fm/fm';

class TopSearch extends React.Component {
  constructor(props, ...rest) {
    super(props, ...rest);
    this.state = {
      exactMatch: false,
      inputValue: props.searchQuery || '',
    };
  }

  convertExactMatch(inputValue) {
    const { exactMatch } = this.state;
    return exactMatch ? `"${inputValue}"` : inputValue;
  }

  toggleExactMatch() {
    this.setState((state) => ({ exactMatch: !state.exactMatch }));

    // const newValue = exactMatch ?
    //   inputValue.slice(1, -1) :
    //   `"${inputValue}"`;
    //
    // this.setState(
    //   { inputValue: newValue },
    //   this.props.doSearch.bind(null, newValue),
    // );
  }

  render() {
    const { doSearch, placeholder, t } = this.props;
    const { inputValue } = this.state;
    const { exactMatch } = this.state;

    return (
      <form
        className="top-search row"
        onSubmit={() => doSearch(this.convertExactMatch(inputValue))}
      >
        <div className="form-group col-sm-6">
          <div className="input-group">
            <input
              type="text"
              className="form-control"
              placeholder={placeholder}
              value={inputValue}
              onChange={(e) => this.setState(
                {
                  inputValue: e.target.value,
                },
              )}
            />
            <div className="input-group-addon">
              <i className="glyphicon glyphicon-search" onClick={() => doSearch(this.convertExactMatch(inputValue))} />
            </div>
          </div>
        </div>
        <div className="form-group exact-match col-sm-6">
          <input
            id="exactMatch"
            type="checkbox"
            checked={exactMatch}
            onChange={() => this.toggleExactMatch()}
          />
            &nbsp;
          <label htmlFor="exactMatch">
            {t('crd:contracts:hint')}
          </label>
        </div>
      </form>
    );
  }
}

TopSearch.propTypes = {
  t: PropTypes.func.isRequired,
};

export default fmConnect(TopSearch, 'crd.textSearch');
