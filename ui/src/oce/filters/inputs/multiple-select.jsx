import React, { useEffect } from 'react';
import { Checkbox, ControlLabel, FormGroup } from 'react-bootstrap';
import { useImmer } from 'use-immer';
import { useTranslation } from 'react-i18next';
import { fetch } from '../../api/Api';
import './styles.css';

const MultipleSelect = (props) => {
  const { ep, value = [], onChange } = props;
  const [options, updateOptions] = useImmer([]);
  const selected = new window.Set(value);

  const getId = (el) => el._id;

  const getLabel = getId;

  useEffect(() => {
    fetch(ep).then((data) => updateOptions(() => data.filter((el) => !!getId(el))));
  }, [ep]);

  const selectAll = () => onChange(options.map(getId));

  const selectNone = () => onChange([]);

  const totalOptions = options.length;
  const selectedCount = options.filter((option, key) => selected.has(getId(option, key))).length;

  const { t } = useTranslation();

  const onToggle = (id) => {
    if (selected.has(id)) {
      onChange(value.filter((el) => el !== id));
    } else {
      onChange(value.concat(id));
    }
  };

  return (
    <FormGroup>
      <ControlLabel className="multiple-select-label">
        Selected
        <span className="count">
          (
          {selectedCount}
          /
          {totalOptions}
          )
        </span>
        <div className="pull-right select-all-none">
          <a onClick={selectAll}>
            {t('filters:multipleSelect:selectAll')}
          </a>
          &nbsp;|&nbsp;
          <a onClick={selectNone}>
            {t('filters:multipleSelect:selectNone')}
          </a>
        </div>
      </ControlLabel>
      {options.map((option, key) => {
        const id = getId(option, key);
        return (
          <Checkbox key={id} onChange={() => onToggle(id)} checked={selected.has(id)}>
            {getLabel(option, key)}
          </Checkbox>
        );
      })}
    </FormGroup>
  );
};

export default MultipleSelect;
