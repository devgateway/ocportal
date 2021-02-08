import React, { useEffect, useRef, useState } from 'react';
import cn from 'classnames';
import { useTranslation } from 'react-i18next';

const DROPDOWN_WIDTH = 500;

const FilterBox = (props) => {
  const {
    title, open, active, onClick, onApply, onReset,
  } = props;

  const { t } = useTranslation();

  const box = useRef();

  const [dropdownMarginLeft, setDropdownMarginLeft] = useState(0);

  const updateDropdownPosition = () => {
    const { clientWidth } = document.documentElement;
    const left = Math.min(0, clientWidth - DROPDOWN_WIDTH - box.current.getBoundingClientRect().left);
    setDropdownMarginLeft(left);
  };

  const handleClick = () => {
    updateDropdownPosition();
    onClick();
  };

  useEffect(() => {
    const update = updateDropdownPosition;
    window.addEventListener('resize', update);
    return () => window.removeEventListener('resize', update);
  }, []);

  const style = {
    marginLeft: dropdownMarginLeft,
  };

  return (
    <div onClick={handleClick} className={cn('filter', { open, active })} ref={box}>
      <span className="box-title">
        {title}
      </span>
      <i className="glyphicon glyphicon-menu-down" />
      {open
      && (
      <div
        className={cn('dropdown')}
        onClick={(e) => e.stopPropagation()}
        style={style}
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
