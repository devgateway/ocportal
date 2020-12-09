import React, {useState} from "react";
import {tCreator} from '../../translatable';
import cn from 'classnames';
import fmConnect from "../../fm/fm";

const FiltersWrapper = props => {

  const [expanded, setExpanded] = useState(new Set());

  const [localFilters, setLocalFilters] = useState(props.filters);

  const toggleItem = index => {
    const expandedVar = new Set(expanded);
    if (expandedVar.has(index)) {
      expandedVar.delete(index);
    } else {
      expandedVar.add(index);
    }

    setExpanded(expandedVar);
  }

  const reset = () => {
    props.applyFilters({});
    setLocalFilters({});
  }

  const apply = () => {
    props.applyFilters(localFilters);
  }

  const t = tCreator(props.translations);

  const listItems = () => {
    const { translations, isFeatureVisible } = props;

    return props.groups
      .filter(group => !group.fm || isFeatureVisible(group.fm))
      .map((group, index) => {
          return (<div
            key={index}
            className={'row filter ' + group.className}>
            <div className={cn('col-md-12 filter-header', { selected: expanded.has(index) })}
                 onClick={_ => toggleItem(index)}>
              <div className="pull-left title">{group.name}</div>
              <div className={'pull-right toggler ' + (expanded.has(index) ? 'up' : 'down')} />
            </div>

            {expanded.has(index) &&
            <div className={cn('col-md-12 filter-content', { expanded: expanded.has(index) })}>
              {group.filters.map((filter, fIdx) =>
                <React.Fragment key={fIdx}>
                  {filter.render({
                    filters: localFilters,
                    onChange: filters => {
                      const newFilters = {
                        ...localFilters,
                        ...filters
                      };
                      setLocalFilters(newFilters);
                    },
                    translations: translations
                  })}
                </React.Fragment>
              )}

              <section className="buttons">
                <button className="btn btn-apply pull-right" onClick={apply}>
                  {t('filters:apply')}
                </button>
                <button className="btn btn-reset pull-right" onClick={reset}>
                  {t('filters:reset')}
                </button>
              </section>
            </div>}
          </div>)
        });
  }

  return (
    <div className={cn('filters', 'col-md-12')}>
      {listItems()}
    </div>
  );
}

export default fmConnect(FiltersWrapper);
