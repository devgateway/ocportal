var webpack = require('webpack');
var path = require('path');

module.exports = {
  entry: {
    lib: [
      'react',
      'react-dom',
      'react-bootstrap',
      'react-bootstrap-table',
      'react-immutable-proptypes',
      'd3',
      'plotly.js-basic-dist',
      'react-leaflet',
      'immutable'
    ]
  },

  output: {
    path: path.join(__dirname, 'dll'),
    filename: '[name].js',
    library: '[name]',
    libraryTarget: 'var'
  },

  plugins: [
    new webpack.DllPlugin({
      path: './dll/manifest.json',
      name: '[name]',
      context: __dirname
    })
  ]
}
