const translatable = (Class) => class Translatable extends Class {
  __(text) {
    console.warn('__ is deprecated, use t');
    const translations = this.props.translations || {};
    return translations[text] || text;
  }

  __n(sg, pl, n) {
    console.warn('__n is deprecated, use t_n');
    return `${n} ${this.__(n === 1 ? sg : pl)}`;
  }

  t(key) {
    const { translations } = this.props;
    if (!translations) console.error('Missing translations', this.constructor.name);
    if (!translations[key]) console.error('Missing translation for key', key);
    return translations[key];
  }

  tMonth(month, years) {
    return `${this.t(`general:months:${month}`)} ${years[0]}`;
  }
};

export default translatable;

export const tCreator = (translations) => (key) => {
  if (!translations) throw new Error('Missing translations');
  if (!translations[key]) throw new Error(`Missing translation for key${key}`);
  return translations[key];
};
