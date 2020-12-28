import React, { useEffect, useRef, useState } from 'react';
import PropTypes from 'prop-types';
import { useImmer } from 'use-immer';
import { tCreator } from '../../translatable';
import {
  dateRendererCreator,
  minMaxPropertyRendererCreator,
  singlePropertyRendererCreator,
} from '../../makueni/filters/FiltersWrapper';
import FilterTenderDate from '../../makueni/filters/FilterTenderDate';
import FilterBox from './box';
import ProcurementMethod from '../../filters/procurement-method';
import ProcurementMethodRationale from '../../filters/procurement-method-rationale';
import { FlaggedTenderPrice } from '../../filters/tender-price';
import { FlaggedAwardValue } from '../../filters/award-value';
import { Buyer, ProcuringEntity, Supplier } from '../../filters/organizations';

const isActiveForFields = (...fields) => (filters) => fields.map((field) => filters[field]).some((value) => (Array.isArray(value)
  ? value.length > 0
  : value != null));

const groups = [
  {
    title: 'filters:tabs:date:title',
    active: isActiveForFields('year'),
    filters: [
      {
        render: dateRendererCreator(FilterTenderDate),
      },
    ],
  },
  {
    title: 'filters:tabs:valueAmount:title',
    active: isActiveForFields('minTenderValue', 'maxTenderValue', 'minAwardValue', 'maxAwardValue'),
    filters: [
      {
        render: minMaxPropertyRendererCreator(FlaggedTenderPrice, 'TenderValue'),
      },
      {
        render: minMaxPropertyRendererCreator(FlaggedAwardValue, 'AwardValue'),
      },
    ],
  },
  {
    title: 'filters:tabs:procurementMethod:title',
    active: isActiveForFields('procurementMethod'),
    filters: [
      {
        render: singlePropertyRendererCreator(ProcurementMethod, 'procurementMethod'),
      },
    ],
  },
  {
    title: 'filters:tabs:procurementMethodRationale:title',
    active: isActiveForFields('procurementMethodRationale'),
    filters: [
      {
        render: singlePropertyRendererCreator(ProcurementMethodRationale, 'procurementMethodRationale'),
      },
    ],
  },
  {
    title: 'filters:tabs:organizations:title',
    active: isActiveForFields('buyerId', 'procuringEntityId', 'supplierId'),
    filters: [
      {
        render: singlePropertyRendererCreator(Buyer, 'buyerId'),
      },
      {
        render: singlePropertyRendererCreator(ProcuringEntity, 'procuringEntityId'),
      },
      {
        render: singlePropertyRendererCreator(Supplier, 'supplierId'),
      },
    ],
  },
];

const Filters = ({ translations, filters, onChange }) => {
  const t = tCreator(translations);

  const [localFilters, updateLocalFilters] = useImmer(filters);

  const [currentBoxIndex, setCurrentBoxIndex] = useState();

  const handleApply = () => {
    setCurrentBoxIndex(null);
    onChange(localFilters);
  };

  const handleReset = () => {
    const newFilters = {};
    updateLocalFilters(() => newFilters);
    setCurrentBoxIndex(null);
    onChange(newFilters);
  };

  const ref = useRef();

  const handleMouseDown = (e) => {
    if (ref.current && !ref.current.contains(e.target)) {
      setCurrentBoxIndex(null);
    }
  };

  useEffect(() => {
    // attaching to dg-container instead of document because dropdowns used in filters are appended to document
    const dgContainer = document.getElementById('dg-container');

    dgContainer.addEventListener('mousedown', handleMouseDown);

    return () => {
      dgContainer.removeEventListener('mousedown', handleMouseDown);
    };
  }, []);

  return (
    <div className="row filters-bar" ref={ref}>
      <div className="col-md-12 crd-horizontal-filters">
        <div className="crd-filter-title">
          <div className="title">{t('filters:hint')}</div>
        </div>
        {groups.map((group, index) => (
          <FilterBox
            key={index}
            title={t(group.title)}
            open={currentBoxIndex === index}
            active={group.active(filters)}
            translations={translations}
            onClick={() => setCurrentBoxIndex(currentBoxIndex === index ? null : index)}
            onApply={handleApply}
            onReset={handleReset}
          >

            {group.filters.map((filter, fIdx) => (
              <React.Fragment key={fIdx}>
                {filter.render({
                  filters: localFilters,
                  onChange: (filters) => updateLocalFilters((draft) => ({ ...draft, ...filters })),
                  translations,
                })}
              </React.Fragment>
            ))}
          </FilterBox>
        ))}
      </div>
    </div>
  );
};

Filters.propTypes = {
  filters: PropTypes.object.isRequired,
  onChange: PropTypes.func.isRequired,
  translations: PropTypes.object.isRequired,
};

export default Filters;
