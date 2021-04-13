const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function (app) {
  app.use(
    createProxyMiddleware((path) => !path.match('^/ui/') && !path.match('^/portal/'), {
      target: 'http://localhost:8090', logLevel: 'debug',
    }),
  );
};
