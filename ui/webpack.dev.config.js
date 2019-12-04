var webpack = require('webpack');
var path = require('path');
module.exports = {
  entry: ['./index.jsx'],
  output: {
    path: path.join(__dirname, 'public/ui'),
    publicPath: "http://localhost:3000/",
    filename: "index.js"
  },
  module: {
    loaders: [
      {
        test: /\.(jsx|es6)$/,
        loaders: [
          'react-hot-loader/webpack',
          'babel-loader?babelrc=true,cacheDirectory'
        ],
        exclude: /node_modules/
      },
      { test: /\.json$/, loader: 'json-loader' },
      { test: /\.css$/, exclude: /\.useable\.css$/, loader: "style-loader!css-loader" },
      { test: /\.less$/, loader: "style-loader!css-loader!less-loader" }
    ]
  },
  resolve: {
    extensions: ['.js', '.es6', '.jsx']
  },
  // devtool: 'source-map',
  devtool: 'cheap-module-eval-source-map',
  plugins: [
    new webpack.LoaderOptionsPlugin({
      options: {
        eslint: {
          configFile: "./.eslintrc"
        }
      }
    }),
    new webpack.DefinePlugin({
      "process.env": {
        NODE_ENV: JSON.stringify("development")
      }
    }),
    new webpack.DllReferencePlugin({
      context: __dirname,
      name: 'lib',
      manifest: require('./dll/manifest.json')
    }),
    new webpack.HotModuleReplacementPlugin(),
    new webpack.ProvidePlugin({
      fetch: 'imports-loader?this=>global!exports-loader?global.fetch!whatwg-fetch',
      React: "react",
    })
  ]
};
