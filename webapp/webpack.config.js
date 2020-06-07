const path = require('path');
module.exports = {
    mode: "development", // to replace "production" in start the service.
    entry: [
        './src/main/resources/assets/index.js'
    ],
    output: {
        path: path.join(__dirname, 'src/main/webapp/docroot/scripts'),
        filename: 'exmp-blog.min.js'
    },
    devtool: 'source-map', // to delete in start the service.
    module: {
        rules: [
            {
              test: /\.scss$/,
              use: [
                  "style-loader", // creates style nodes from JS strings
                  "css-loader", // translates CSS into CommonJS
                  "sass-loader" // compiles Sass to CSS, using Node Sass by default
              ]
          },
          {
              test: /\.less$/,
              use: [
                  "style-loader", // creates style nodes from JS strings
                  "css-loader", // translates CSS into CommonJS
                  "less-loader" // compiles Less to CSS
              ]
          }
        ]
    }
};