import FilterBox from './box';
import { Set } from 'immutable';
import ProcurementMethod from '../../filters/procurement-method.jsx';
import ProcurementMethodRationale from '../../filters/procurement-method-rationale';

class ProcurementMethodRationaleBox extends FilterBox {
  isActive() {
    const { appliedFilters } = this.props;
    return appliedFilters.get('procurementMethodRationale', Set())
    .count() > 0;
  }

  reset() {
    const { onApply, state } = this.props;
    onApply(state.delete('procurementMethodRationale'));
  }

  getTitle() {
    return this.t('filters:tabs:procurementMethodRationale:title');
  }

  getBox() {
    return (
      <div className="box-content">
        {this.renderChild(ProcurementMethodRationale, 'procurementMethodRationale')}
      </div>
    );
  }
}

export default ProcurementMethodRationaleBox;
