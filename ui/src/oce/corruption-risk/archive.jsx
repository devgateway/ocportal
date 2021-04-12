import cn from 'classnames';
import CRDPage from './page';
import TopSearch from './top-search';
import { wireProps } from './tools';

class Archive extends CRDPage {
  render() {
    const {
      className, searchQuery, doSearch, topSearchPlaceholder, t, data, List, dataEP,
      countEP,
    } = this.props;

    const count = data.get('count');

    return (
      <div className={cn(className)}>
        <TopSearch
          t={t}
          searchQuery={searchQuery ? decodeURI(searchQuery) : ''}
          doSearch={doSearch}
          placeholder={topSearchPlaceholder}
        />

        {searchQuery && (
        <h3 className="page-header">
          {
            (count === 1
              ? t('crd:contracts:top-search:resultsFor:sg')
              : t('crd:contracts:top-search:resultsFor:pl')
            ).replace('$#$', count).replace(
              '$#$',
              searchQuery.replace(/%22/g, '').replace(/%20/g, ' '),
            )
}
        </h3>
        )}

        <List
          {...wireProps(this)}
          dataEP={dataEP}
          countEP={countEP}
          searchQuery={searchQuery}
        />
      </div>
    );
  }
}

export default Archive;
