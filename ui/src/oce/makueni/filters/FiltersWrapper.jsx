import React from 'react';
import { useImmer } from 'use-immer';
import cn from 'classnames';
import { tCreator } from '../../translatable';
import fmConnect from '../../fm/fm';

export const singlePropertyRendererCreator = (FilterItem, property) => ({ filters, onChange, ...props }) => <FilterItem value={filters[property]} onChange={(value) => onChange({ [property]: value })} {...props} />;

export const minMaxPropertyRendererCreator = (FilterItem, suffix) => ({ filters, onChange, ...props }) => {
  const minProperty = `min${suffix}`;
  const maxProperty = `max${suffix}`;
  return (
    <FilterItem
      minValue={filters[minProperty]}
      maxValue={filters[maxProperty]}
      minProperty={minProperty}
      maxProperty={maxProperty}
      onChange={({ minValue, maxValue }) => onChange({ [minProperty]: minValue, [maxProperty]: maxValue })}
      {...props}
    />
  );
};

export const dateRendererCreator = (FilterItem) => ({ filters, onChange, ...props }) => (
  <FilterItem
    years={filters.year}
    months={filters.month}
    onChange={
                (value) => onChange({
                  year: value.years,
                  month: value.months,
                  monthly: value.years.length === 1,
                })
              }
    {...props}
  />
);

const FiltersWrapper = (props) => {
  const [expanded, updateExpanded] = useImmer(new Set());

  const [localFilters, updateLocalFilters] = useImmer(props.filters);

  const toggleItem = (index) => {
    updateExpanded((draft) => {
      if (draft.has(index)) {
        draft.delete(index);
      } else {
        draft.add(index);
      }
    });
  };

  const reset = () => {
    props.applyFilters({});
    updateLocalFilters(() => ({}));
  };

  const apply = () => {
    props.applyFilters(localFilters);
  };

  const t = tCreator(props.translations);

  const listItems = () => {
    const { translations, isFeatureVisible } = props;

    return props.groups
      .filter((group) => !group.fm || isFeatureVisible(group.fm))
      .map((group, index) => (
        <div
          key={index}
          className={`row filter ${group.className}`}
        >
          <div
            className={cn('col-md-12 filter-header', { selected: expanded.has(index) })}
            onClick={() => toggleItem(index)}
          >
            <div className="pull-left title">{group.name}</div>
            <div className={`pull-right toggler ${expanded.has(index) ? 'up' : 'down'}`} />
          </div>

          {expanded.has(index)
            && (
            <div className={cn('col-md-12 filter-content', { expanded: expanded.has(index) })}>
              {group.filters.map((filter, fIdx) => (
                <React.Fragment key={fIdx}>
                  {filter.render({
                    filters: localFilters,
                    onChange: (filters) => updateLocalFilters((draft) => ({ ...draft, ...filters })),
                    translations,
                  })}
                </React.Fragment>
              ))}

              <section className="buttons">
                <button className="btn btn-apply pull-right" onClick={apply}>
                  {t('filters:apply')}
                </button>
                <button className="btn btn-reset pull-right" onClick={reset}>
                  {t('filters:reset')}
                </button>
              </section>
            </div>
            )}
        </div>
      ));
  };

  return (
    <div className={cn('filters', 'col-md-12')}>
      {listItems()}
    </div>
  );
};

export default fmConnect(FiltersWrapper);
