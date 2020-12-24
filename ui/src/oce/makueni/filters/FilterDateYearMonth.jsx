import React, { useEffect, useState } from 'react';
import cn from 'classnames';
import PropTypes from 'prop-types';
import { pluck, range, sameArray } from '../../tools';
import { fetch } from '../../api/Api';
import { tCreator } from '../../translatable';

const FilterDateYearMonth = (props) => {
  const monthRange = range(1, 12);
  const [allYears, setAllYears] = useState([]);
  const t = tCreator(props.translations);

  const { ep } = props;
  const years = (props.years.length > 0) ? props.years : allYears;
  const months = (props.months.length > 0) ? props.months : monthRange;

  /**
     * Retrieve allYears from the API.
     */
  useEffect(() => {
    fetch(ep).then((data) => {
      const years = data.map(pluck('_id')).sort();
      setAllYears(years);
    });
  }, [ep]);

  const showMonths = () => years.filter((x) => allYears.includes(x)).length === 1;

  const handleOnChange = (years, months) => props.onChange({
    years: sameArray(years, allYears) ? [] : years,
    months: (years.length > 1 || sameArray(months, monthRange)) ? [] : months,
  });

  const monthsBar = () => monthRange.map((month) => {
    const included = months.includes(month);
    return (
      <a
        key={month}
        className={cn('col-md-3', { active: included })}
        onClick={() => handleOnChange(
          years,
          included
            ? months.filter((item) => item !== month)
            : [...months, month],
        )}
      >
        <i className="glyphicon glyphicon-ok-circle" />
        {' '}
        {t(`general:months:${month}`)}
      </a>
    );
  });

  const yearsBar = () => {
    const toggleYear = (year) => handleOnChange(
      years.includes(year)
        ? years.filter((item) => item !== year)
        : [...years, year],
      months,
    );

    const toggleOthersYears = (year) => handleOnChange(
      years.length === 1 && years.includes(year) ? [] : [year],
      months,
    );

    return allYears
      .map((year) => (
        <a
          key={year}
          className={cn('col-md-3', { active: years.includes(year) })}
          onDoubleClick={() => toggleOthersYears(year)}
          onClick={(e) => (e.shiftKey ? toggleOthersYears(year) : toggleYear(year))}
        >
          <i className="glyphicon glyphicon-ok-circle" />
          {' '}
          {year}
        </a>
      ));
  };

  return (
    <div className="date-filter">
      <div className="years-bar" role="navigation">
        {yearsBar()}
      </div>

      <div className="row">
        <div className="hint col-md-12">
          {t('yearsBar:ctrlClickHint')}
        </div>
      </div>

      {showMonths() === true
        ? (
          <div className="months-bar" role="navigation">
            {monthsBar()}
          </div>
        )
        : null}
    </div>
  );
};

FilterDateYearMonth.defaultProps = {
  years: [],
  months: [],
};

FilterDateYearMonth.propTypes = {
  translations: PropTypes.object.isRequired,
  ep: PropTypes.string.isRequired,
  onChange: PropTypes.func.isRequired,
  years: PropTypes.arrayOf(PropTypes.number),
  months: PropTypes.arrayOf(PropTypes.number),
};

export default FilterDateYearMonth;
