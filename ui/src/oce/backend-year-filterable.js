let backendFilterable = Class => class extends Class{
  buildUrl(ep){
    const {years, months, monthly} = this.props;
    const yearDecoratedUrl = super.buildUrl(ep).addSearch('year', years);
    return monthly ?
        yearDecoratedUrl.addSearch('monthly', true).addSearch('month', months) :
        yearDecoratedUrl;
  }

  componentDidUpdate(prevProps){
    const shouldRefetch = ['years', 'months', 'monthly'].some(prop => this.props[prop] != prevProps[prop]);
    if(shouldRefetch){
      this.fetch()
    } else super.componentDidUpdate(prevProps);
  }
};

export default backendFilterable;