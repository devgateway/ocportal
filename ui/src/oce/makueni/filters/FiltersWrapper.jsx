import translatable from '../../translatable';
import Component from '../../pure-render-component';
import cn from 'classnames';
import { Map } from 'immutable';

class FiltersWrapper extends translatable(Component) {
  constructor(props) {
    super(props);

    this.state = {
      expanded: new Set(),
      localFilters: Map()
    };
  }

  toggleItem(index) {
    const expanded = new Set(this.state.expanded);
    if (expanded.has(index)) {
      expanded.delete(index);
    } else {
      expanded.add(index);
    }

    this.setState({ expanded: expanded });
  }

  reset() {
    this.resetPage();
    this.props.filters.assign('[[FiltersWrapper]]', Map());
    this.setState({
      localFilters: Map()
    });
  }

  resetPage() {
    this.props.resetPage && this.props.resetPage();
  }

  apply() {
    this.resetPage();
    this.props.filters.assign('[[FiltersWrapper]]', this.state.localFilters);
  }

  listItems() {
    const { expanded } = this.state;
    const { translations, isFeatureVisible } = this.props;

    return this.constructor.ITEMS
      .filter((Item, index) => isFeatureVisible(this.constructor.FM[index]))
      .map((Item, index) => <div
        key={index}
        className={'row filter ' + this.constructor.CLASS[index]}>
        <div className={cn('col-md-12 filter-header', { selected: expanded.has(index) })}
             onClick={_ => this.toggleItem(index)}>
          <div className="pull-left title">{Item.getName(this.t.bind(this))}</div>
          <div className={'pull-right toggler ' + (expanded.has(index) ? 'up' : 'down')}></div>
        </div>

        <div className={cn('col-md-12 filter-content', { expanded: expanded.has(index) })}>
          <Item translations={translations} filters={this.props.filters}
                localFilters={this.state.localFilters}
                onUpdate={(key, update) => {
                  this.setState({ localFilters: this.state.localFilters.set(key, update) }) }
                }
          />

          <section className="buttons">
            <button className="btn btn-apply pull-right" onClick={e => this.apply()}>
              {this.t('filters:apply')}
            </button>
            <button className="btn btn-reset pull-right" onClick={e => this.reset()}>
              {this.t('filters:reset')}
            </button>
          </section>
        </div>
      </div>
    );
  }

  render() {
    return <div className={cn('filters', 'col-md-12')}>
      {this.listItems()}
    </div>;
  }
}

export default FiltersWrapper;
