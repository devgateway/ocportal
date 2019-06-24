import Component from '../pure-render-component';
import translatable from '../translatable';
import cn from 'classnames';
import Organizations from './tabs/organizations';
import ProcurementRules from './tabs/procurement-rules.jsx';
import Amounts from './tabs/amounts';
import { Map } from 'immutable';
import URI from 'urijs';

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
  
  listTabs() {
    const { expanded } = this.state;
    return this.constructor.TABS.map((Tab, index) => <div
        key={index}
        className={cn('col-md-12 box', { active: expanded.has(index) })}
        onClick={_ => this.toggleTab(index)}>
        <span className="title">{Tab.getName(this.t.bind(this))}</span>
        
        {expanded.has(index)
          ? this.content(index)
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
      <section className="buttons col-xs-offset-4 col-xs-8">
        <button className="btn btn-danger" onClick={e => onUpdate(this.state.state)}>
          {this.t('filters:apply')}
        </button>
        &nbsp;
        <button className="btn btn-default" onClick={e => this.reset()}>
          {this.t('filters:reset')}
        </button>
      </section>
    </div>);
  }
  
  reset() {
    this.setState({
      expanded: new Set(),
      state: Map()
    });
    this.props.onUpdate(Map());
  }
  
  updateFilters(newFilters) {
    this.setState({ state: newFilters });
    this.props.onUpdate(newFilters);
  }
  
  render() {
    console.log(this.state);
    
    return <div className={cn('filters')}>
      <div className="">
        {this.listTabs()}
      </div>
    </div>;
  }
}

Filters.TABS = [Organizations, ProcurementRules, Amounts];

export default Filters;
