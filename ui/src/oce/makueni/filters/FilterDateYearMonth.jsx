import React, {useEffect, useState} from "react";
import {pluck, range} from '../../tools';
import cn from 'classnames';
import {fetch} from "../../api/Api";
import {tCreator} from "../../translatable";
import PropTypes from "prop-types";

const FilterDateYearMonth = props => {
    const monthRange = range(1, 12);
    const [selectedMonths, setSelectedMonths] = useState(monthRange);
    const [years, setYears] = useState([]);
    const [selectedYears, setSelectedYears] = useState([]);
    const t = tCreator(props.translations);

    /**
     * When loading endpoint, set years and selected years
     */
    useEffect(() => {
        fetch(props.ep).then(data => {
            const years = data.map(pluck('_id'));
            setYears(years);
            setSelectedYears(years);
        });
    }, [props.ep]);

    /**
     * When resetting year, reset both selected years and months
     */
    useEffect(() => {
        if (props.year === undefined) {
            setSelectedYears(years);
            setSelectedMonths(monthRange);
        }
    }, [props.year, props.month, props.ep]);

    /**
     * When selection of years/months change, invoke onChange on parent
     */
    useEffect(() => {
        if (selectedYears.length === 0) {
            return;
        }
        if (selectedYears.length === 1) {
            props.onChange({month: selectedMonths, year: selectedYears});
        } else {
            props.onChange({year: selectedYears});
        }
    }, [selectedYears, selectedMonths]);

    const showMonths = () => {
        if (years === undefined) {
            return false;
        }
        return selectedYears.filter(x => years.includes(x)).length === 1;
    }

    const monthsBar = () => {
        return monthRange.map(month => (<a
            key={month}
            className={cn('col-md-3', {active: selectedMonths.includes(month)})}
            onClick={() => {
                selectedMonths.includes(month) ? setSelectedMonths(selectedMonths.filter(item => item !== month))
                    : setSelectedMonths({...selectedMonths, month});
            }}
        >
            <i className="glyphicon glyphicon-ok-circle"/> {t(`general:months:${month}`)}
        </a>));
    }

    const yearsBar = () => {
        const toggleYear = year => {
            selectedYears.includes(year) ? setSelectedYears(selectedYears.filter(item => item !== year))
                : setSelectedYears({...selectedYears, year});
        };

        const toggleOthersYears = year => {
            setSelectedYears(selectedYears.length === 1 && selectedYears.includes(year) ? years : [year]);
        };

        return years.sort()
            .map(year =>
                (<a
                    key={year}
                    className={cn('col-md-3', {active: selectedYears.includes(year)})}
                    onDoubleClick={() => toggleOthersYears(year)}
                    onClick={e => (e.shiftKey ? toggleOthersYears(year) : toggleYear(year))}
                >
                    <i className="glyphicon glyphicon-ok-circle"/> {year}
                </a>))
            .reduce((arr, item) => {
                arr.push(item);
                return arr;
            }, []);
    }

    if (years === undefined) {
        return null;
    }

    return (<div className="date-filter">
        <div className="row years-bar" role="navigation">
            {yearsBar()}
        </div>

        <div className="row">
            <div className="hint col-md-12">
                {t('yearsBar:ctrlClickHint')}
            </div>
        </div>

        {showMonths() === true
            ? <div className="row months-bar" role="navigation">
                {monthsBar()}
            </div>
            : null
        }
    </div>);
}

FilterDateYearMonth.propTypes = {
    translations: PropTypes.object.isRequired,
    ep: PropTypes.string.isRequired,
    onChange: PropTypes.func.isRequired,
    year: PropTypes.array,
    month: PropTypes.array,
    monthly: PropTypes.bool
};

export default FilterDateYearMonth;
