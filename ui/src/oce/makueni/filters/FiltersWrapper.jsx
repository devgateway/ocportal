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

    return props.items
      .filter((item) => isFeatureVisible(item.fm))
      .map((item, index) => {
          return (<div
            key={index}
            className={'row filter ' + item.className}>
            <div className={cn('col-md-12 filter-header', { selected: expanded.has(index) })}
                 onClick={_ => toggleItem(index)}>
              <div className="pull-left title">{item.name}</div>
              <div className={'pull-right toggler ' + (expanded.has(index) ? 'up' : 'down')} />
            </div>

            <div className={cn('col-md-12 filter-content', { expanded: expanded.has(index) })}>
              {item.render({
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

              <section className="buttons">
                <button className="btn btn-apply pull-right" onClick={apply}>
                  {t('filters:apply')}
                </button>
                <button className="btn btn-reset pull-right" onClick={reset}>
                  {t('filters:reset')}
                </button>
              </section>
            </div>
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
