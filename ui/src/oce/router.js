const PREFIX = '#!';
const listeners = [];

export const onNavigation = (listener) => {
  listeners.push(listener);
};

export const getRoute = () => {
  const raw = window.location.hash.split('/');
  const [maybePrefix, ...params] = raw;
  return maybePrefix === PREFIX ? params : [];
};

export const navigate = (...params) => {
  window.location.hash = `${PREFIX}/${params.join('/')}`;
};

window.addEventListener('popstate', () => {
  const route = getRoute();
  listeners.forEach(listener => listener(route));
});
