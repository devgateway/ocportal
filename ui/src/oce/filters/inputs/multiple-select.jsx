import React, { useEffect } from 'react';
import { Form } from 'react-bootstrap';
import { useImmer } from 'use-immer';
import { useTranslation } from 'react-i18next';
import { fetch } from '../../api/Api';
import './styles.css';

const MultipleSelect = ({ ep, value = [], onChange }) => {
  const [options, updateOptions] = useImmer([]);
  const selected = new Set(value);

  const getId = (el) => el._id;
  const getLabel = (el) => el.name || el._id;

  useEffect(() => {
    fetch(ep)
      .then((data) => updateOptions(() => data.filter((el) => !!getId(el))))
      .catch((err) => console.error('Failed to fetch options:', err));
  }, [ep]);

  const selectAll = () => onChange(options.map(getId));
  const selectNone = () => onChange([]);

  const totalOptions = options.length;
  const selectedCount = options.filter((option) => selected.has(getId(option))).length;

  const { t } = useTranslation();

  const onToggle = (id) => {
    onChange(selected.has(id) ? value.filter((el) => el !== id) : [...value, id]);
  };

  return (
    <Form.Group controlId="multipleSelect">
      <Form.Label className="multiple-select-label">
        Selected
        <span className="count">
          (
          {selectedCount}
          /
          {totalOptions}
          )
        </span>
        <div className="pull-right select-all-none">
          <button type="button" className="btn-link" onClick={selectAll}>
            {t('filters:multipleSelect:selectAll')}
          </button>
            &nbsp;|&nbsp;
          <button type="button" className="btn-link" onClick={selectNone}>
            {t('filters:multipleSelect:selectNone')}
          </button>
        </div>
      </Form.Label>
      {options.map((option) => {
        const id = getId(option);
        return (
          <Form.Check
            type="checkbox"
            key={id}
            onChange={() => onToggle(id)}
            checked={selected.has(id)}
            label={getLabel(option)}
          />
        );
      })}
    </Form.Group>
  );
};

export default MultipleSelect;
