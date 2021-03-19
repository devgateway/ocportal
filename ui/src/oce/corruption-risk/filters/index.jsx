import React, { useEffect, useRef, useState } from 'react';
import PropTypes from 'prop-types';
import { useImmer } from 'use-immer';
import { useTranslation } from 'react-i18next';
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
import fmConnect from '../../fm/fm';

const isActiveForFields = (...fields) => (filters) => fields
  .map((field) => filters[field])
  .some((value) => (Array.isArray(value) ? value.length > 0 : value != null));

const groups = [
  {
    title: 'filters:tabs:date:title',
    active: isActiveForFields('year'),
    fm: 'crd.filters.dateGroup',
    filters: [
      {
        render: dateRendererCreator(FilterTenderDate),
        fm: 'crd.filters.date',
      },
    ],
  },
  {
    title: 'filters:tabs:valueAmount:title',
    active: isActiveForFields('minTenderValue', 'maxTenderValue', 'minAwardValue', 'maxAwardValue'),
    fm: 'crd.filters.valuesGroup',
    filters: [
      {
        render: minMaxPropertyRendererCreator(FlaggedTenderPrice, 'TenderValue'),
        fm: 'crd.filters.tenderValue',
      },
      {
        render: minMaxPropertyRendererCreator(FlaggedAwardValue, 'AwardValue'),
        fm: 'crd.filters.awardValue',
      },
    ],
  },
  {
    title: 'filters:tabs:procurementMethod:title',
    active: isActiveForFields('procurementMethod'),
    fm: 'crd.filters.procurementMethodGroup',
    filters: [
      {
        render: singlePropertyRendererCreator(ProcurementMethod, 'procurementMethod'),
        fm: 'crd.filters.procurementMethod',
      },
    ],
  },
  {
    title: 'filters:tabs:procurementMethodRationale:title',
    active: isActiveForFields('procurementMethodRationale'),
    fm: 'crd.filters.procurementMethodRationaleGroup',
    filters: [
      {
        render: singlePropertyRendererCreator(ProcurementMethodRationale, 'procurementMethodRationale'),
        fm: 'crd.filters.procurementMethodRationale',
      },
    ],
  },
  {
    title: 'filters:tabs:organizations:title',
    active: isActiveForFields('buyerId', 'procuringEntityId', 'supplierId'),
    fm: 'crd.filters.orgsGroup',
    filters: [
      {
        render: singlePropertyRendererCreator(Buyer, 'buyerId'),
        fm: 'crd.filters.buyer',
      },
      {
        render: singlePropertyRendererCreator(ProcuringEntity, 'procuringEntityId'),
        fm: 'crd.filters.procuringEntity',
      },
      {
        render: singlePropertyRendererCreator(Supplier, 'supplierId'),
        fm: 'crd.filters.supplier',
      },
    ],
  },
];

const Filters = ({
  filters, onChange, isFeatureVisible,
}) => {
  const { t } = useTranslation();

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

  const visibleGroups = groups
    .filter((group) => isFeatureVisible(group.fm))
    .map((group) => ({
      ...group,
      filters: group.filters.filter((f) => isFeatureVisible(f.fm)),
    }));

  return (
    <div className="row filters-bar" ref={ref}>
      <div className="col-md-12 crd-horizontal-filters">
        <div className="crd-filter-title">
          <div className="title">{t('filters:hint')}</div>
        </div>
        {visibleGroups.map((group, index) => (
          <FilterBox
            key={index}
            title={t(group.title)}
            open={currentBoxIndex === index}
            active={group.active(filters)}
            onClick={() => setCurrentBoxIndex(currentBoxIndex === index ? null : index)}
            onApply={handleApply}
            onReset={handleReset}
          >

            {group.filters.map((filter, fIdx) => (
              <React.Fragment key={fIdx}>
                {filter.render({
                  filters: localFilters,
                  onChange: (newFilters) => updateLocalFilters((draft) => ({ ...draft, ...newFilters })),
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
  isFeatureVisible: PropTypes.func.isRequired,
};

export default fmConnect(Filters);
