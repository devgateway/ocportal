import Component from '../pure-render-component';
import translatable from '../translatable';
import cn from 'classnames';
import Organizations from './tabs/organizations';
import ProcurementMethodRules from './tabs/procurement-method.jsx';
import Amounts from './tabs/amounts';
import { Map } from 'immutable';
import URI from 'urijs';
import FilterChartsTab from './tabs/date';
import PlanningRules from './tabs/planning-rules';
import ProcurementMethodRationaleRules from './tabs/procurement-method-rationale';

const dashboardId = new URI(location).search(true).dashboardId;

class Filters extends translatable(Component) {
  constructor(props) {
    super(props);
    this.state = {
      expanded: new Set(),
      state: Map(),
    };
  }

  toggleTab(index) {
    const expanded = new Set(this.state.expanded);
    if (expanded.has(index)) {
      expanded.delete(index);
    } else {
      expanded.add(index);
    }

    this.setState({ expanded: expanded });
  }

  reset() {
    let { filters } = this.props;
    //will always retain locationType property. This is not supposed to be reset because it is
    //never selected by the filter panel, but by the map button.
    let newState = Map().set('locationType',filters.get('locationType'));
    this.setState({
      state: newState
    });

    this.props.onUpdate(newState);
  }

  listTabs() {
    const { expanded } = this.state;
    return this.constructor.TABS.map((Tab, index) => <div
        key={index}
        className={"row filter " + this.constructor.CLASS[index]}>
        <div className={cn('col-md-12 filter-header', { selected: expanded.has(index) })}
             onClick={_ => this.toggleTab(index)}>
          <div className="pull-left title">{Tab.getName(this.t.bind(this))}</div>
          <div className={'pull-right toggler ' + (expanded.has(index) ? 'up' : 'down')}></div>
        </div>

        {expanded.has(index)
          ? <div className="col-md-12">{this.content(index)}</div>
          : null
        }
      </div>
    );
  }

  content(tabIndex) {
    let { state } = this.state;
    let { onUpdate, bidTypes, translations } = this.props;
    let Component = this.constructor.TABS[tabIndex];
    return (<div>
      <Component
        state={state}
        onUpdate={(key, update) => this.setState({ state: this.state.state.set(key, update) })}
        bidTypes={bidTypes}
        translations={translations}/>
      <section className="buttons">
        <button className="btn btn-apply pull-right" onClick={e => onUpdate(this.state.state)}>
          {this.t('filters:apply')}
        </button>
        <button className="btn btn-reset pull-right" onClick={e => this.reset()}>
          {this.t('filters:reset')}
        </button>
      </section>
    </div>);
  }

  render() {
    return <div className={cn('filters', 'col-md-12')}>
      {this.listTabs()}
    </div>;
  }
}

Filters.TABS = [Organizations, ProcurementMethodRules, ProcurementMethodRationaleRules, PlanningRules, Amounts, FilterChartsTab];
Filters.CLASS = ['organizations', 'procurement-method', 'procurement-method-rationale', 'fiscal-year', 'amounts', 'date'];

export default Filters;
