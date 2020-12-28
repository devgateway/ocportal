import React from 'react';
import cn from 'classnames';
import { tCreator } from '../../../translatable';
import flag from '../../../resources/icons/flag.svg';

class Cell extends React.PureComponent {
  render() {
    const {
      title, children, dlClassName, ...props
    } = this.props;
    return (
      <td {...props}>
        <dl className={dlClassName}>
          <dt>{title}</dt>
          <dd>{children}</dd>
        </dl>
      </td>
    );
  }
}

const Info = ({
  info, flagsCount, prs, contractsCount, unflaggedContractsCount, translations,
}) => {
  if (!info) return null;
  const { address, contactPoint } = info;

  const t = tCreator(translations);

  const showBottomTable = address || contactPoint;

  return (
    <div className="pe-page">
      <section className="info">
        <table className={cn('table', 'table-bordered', 'info-table', { 'join-bottom': showBottomTable })}>
          <tbody>
            <tr>
              <Cell title={t('crd:contracts:baseInfo:buyerName')}>
                {info.name}
              </Cell>
              <Cell title={t('crd:suppliers:ID')}>{info.id}</Cell>
              <td className="flags">
                <img src={flag} alt="Flag icon" className="flag-icon" />
              &nbsp;
                <span className="count">
                  {flagsCount}
                &nbsp;
                  {t(flagsCount === 1
                    ? 'crd:contracts:baseInfo:flag:sg'
                    : 'crd:contracts:baseInfo:flag:pl')}
                </span>
                <small>
                  {contractsCount}
                  {' '}
                  procurements flagged
                  <br />
                  (Out of
                  {' '}
                  {unflaggedContractsCount}
                  {' '}
                  procurement won)
                </small>
              </td>
            </tr>
            {prs && prs.length
          && (
          <tr>
            <Cell title={t('crd:contracts:baseInfo:procuringEntityName')} colSpan="3">
              {prs.map((pr) => <p key={pr.procuringEntityId}>{pr.procuringEntityName}</p>)}
            </Cell>
          </tr>
          )}
          </tbody>
        </table>
        {showBottomTable
        && (
        <table className="table table-bordered info-table">
          <tbody>
            <tr>
              {address
            && (
            <Cell title="Address" dlClassName="smaller">
              {address.streetAddress}
              {' '}
              <br />
              {address.locality}
              {' '}
              /
              &nbsp;
              {address.postalCode}
              {' '}
              /
              &nbsp;
              {address.countryName}
            </Cell>
            )}
              {contactPoint
            && (
            <Cell title="Contacts" colSpan="2" dlClassName="smaller">
              {contactPoint.name}
              <br />
              {contactPoint.email}
              <br />
              {contactPoint.telephone}
            </Cell>
            )}
            </tr>
          </tbody>
        </table>
        )}
      </section>
    </div>
  );
};

export default Info;
