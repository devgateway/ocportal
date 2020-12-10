import {tCreator} from '../translatable';
import FiltersWrapper from "../makueni/filters/FiltersWrapper";
import React from "react";
import ProcuringEntitySelect from "./procuring-entity-select";
import BuyerSelect from "./buyer-select";
import SupplierSelect from "./supplier-select";
import {
  minMaxPropertyRendererCreator,
  singlePropertyRendererCreator
} from "../makueni/filters/FiltersProcurementPlanWrapper";
import ProcurementMethod from "./procurement-method";
import ProcurementMethodRationale from "./procurement-method-rationale";
import FiscalYear from "./fiscal-year";
import TenderPrice from "./tender-price";
import AwardValue from "./award-value";
import {dateRendererCreator} from "../makueni/filters/FiltersTendersWrapper";
import FilterTenderDate from "../makueni/filters/FilterTenderDate";

const Filters = props => {

  const t = tCreator(props.translations);

  const groups = [
    {
      name: t('filters:tabs:organizations:title'),
      className: 'organizations',
      filters: [
        {
          render: singlePropertyRendererCreator(ProcuringEntitySelect, 'procuringEntityId')
        },
        {
          render: singlePropertyRendererCreator(BuyerSelect, 'buyerId')
        },
        {
          render: singlePropertyRendererCreator(SupplierSelect, 'supplierId')
        }
      ]
    },
    {
      name: t('filters:tabs:procurementMethod:title'),
      className: 'procurement-method',
      filters: [
        {
          render: singlePropertyRendererCreator(ProcurementMethod, 'procurementMethod')
        }
      ]
    },
    {
      name: t('filters:tabs:procurementMethodRationale:title'),
      className: 'procurement-method-rationale',
      filters: [
        {
          render: singlePropertyRendererCreator(ProcurementMethodRationale, 'procurementMethodRationale')
        }
      ]
    },
    {
      name: t('filters:tabs:fiscalYear:title'),
      className: 'fiscal-year',
      filters: [
        {
          render: singlePropertyRendererCreator(FiscalYear, 'fiscalYear')
        }
      ]
    },
    {
      name: t('filters:tabs:amounts:title'),
      className: 'amounts',
      filters: [
        {
          render: minMaxPropertyRendererCreator(TenderPrice, 'TenderValue')
        },
        {
          render: minMaxPropertyRendererCreator(AwardValue, 'AwardValue')
        }
      ]
    },
    {
      name: t('filters:tabs:date:title'),
      className: 'date',
      filters: [
        {
          render: dateRendererCreator(FilterTenderDate)
        }
      ]
    }
  ];

  return <FiltersWrapper
    groups={groups} filters={props.filters} applyFilters={props.onUpdate} translations={props.translations} />
};

export default Filters;
