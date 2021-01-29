import React from 'react';
import cn from 'classnames';
import { useTranslation } from 'react-i18next';

const FilterBox = (props) => {
  const {
    title, open, active, onClick, onApply, onReset, right,
  } = props;

  const { t } = useTranslation();

  return (
    <div onClick={onClick} className={cn('filter', { open, active })}>
      <span className="box-title">
        {title}
      </span>
      <i className="glyphicon glyphicon-menu-down" />
      {open
      && (
      <div
        className={cn('dropdown', { 'dropdown-r': right })}
        onClick={(e) => e.stopPropagation()}
      >
        <div className="box-content">
          {props.children}
        </div>
        <div className="controls">
          <button
            className="btn btn-reset"
            onClick={onReset}
          >
            {t('filters:reset')}
          </button>
          &nbsp;
          <button
            className="btn btn-apply"
            onClick={onApply}
          >
            {t('filters:apply')}
          </button>
        </div>
      </div>
      )}
    </div>
  );
};

export default FilterBox;
